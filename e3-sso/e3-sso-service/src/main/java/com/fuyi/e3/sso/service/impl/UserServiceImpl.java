package com.fuyi.e3.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.fuyi.e3.common.jedis.JedisClient;
import com.fuyi.e3.common.utils.JsonUtils;
import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.mapper.TbUserMapper;
import com.fuyi.e3.pojo.TbUser;
import com.fuyi.e3.pojo.TbUserExample;
import com.fuyi.e3.pojo.TbUserExample.Criteria;
import com.fuyi.e3.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${USER_INFO_PRE}")
	private String USER_INFO_PRE;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public TaotaoResult checkData(String param, int type) {

		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 1、2、3分别代表username、phone、email
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(param);
			break;
		case 2:
			criteria.andPhoneEqualTo(param);
			break;
		case 3:
			criteria.andEmailEqualTo(param);
			break;
		default:
			return TaotaoResult.build(400, "非法的参数");
		}

		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	@Override
	public TaotaoResult createUser(TbUser user) {

		// 判空
		if (user.getUsername() == null || "".equals(user.getUsername())) {
			return TaotaoResult.build(400, "用户名不能为空");
		}
		if (user.getPassword() == null || "".equals(user.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空");
		}

		// 校验是否可用
		TaotaoResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "此用户名已经被占用");
		}

		// 校验电话是否可以
		if (user.getPhone() != null && !"".equals(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "此手机号已经被使用");
			}
		}

		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 4、把用户信息插入到数据库中。
		userMapper.insert(user);
		// 5、返回e3Result。
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		// 1、判断用户名密码是否正确。
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		// 查询用户信息
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);

		// 校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}

		// 2.登录成功后生成token, Token相当于原来的jsessionid,字符串，可以使用uuid
		String token = UUID.randomUUID().toString();

		// 3.把用户信息保存到redis, key就是token，value就是TbUser对像转成json
		// 4、使用String类型保存Session信息。可以使用“前缀:token”为key
		user.setPassword(null);
		jedisClient.set(USER_INFO_PRE + ":" + token, JsonUtils.objectToJson(user));
		// 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
		jedisClient.expire(USER_INFO_PRE + ":" + token, SESSION_EXPIRE);

		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		// 2、根据token查询redis。
		String json = jedisClient.get(USER_INFO_PRE + ":" + token);
		if(json == null || "".equals(json)) {
			// 3、如果查询不到数据。返回用户已经过期。
			return TaotaoResult.build(400, "用户登录已经过期，请重新登录");
		}
		// 4、如果查询到数据，说明用户已经登录。
		// 5、需要重置key的过期时间。
		jedisClient.expire(USER_INFO_PRE + ":" + token, SESSION_EXPIRE);
		
		// 6、把json数据转换成TbUser对象，然后使用e3Result包装并返回。
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);

		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		
		jedisClient.del(USER_INFO_PRE + ":" + token);
		
		return TaotaoResult.ok();
	}

}
