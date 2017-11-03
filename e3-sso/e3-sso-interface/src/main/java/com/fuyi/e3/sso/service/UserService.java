package com.fuyi.e3.sso.service;

import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.pojo.TbUser;

public interface UserService {

	/**
	 * 校验注册用户
	 * @param param 参数  
	 * @param type 类型    1、2、3分别代表username、phone、email
	 * @return
	 */
	public TaotaoResult checkData(String param, int type);
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public TaotaoResult createUser(TbUser user);
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public TaotaoResult login(String username, String password);
	
	/**
	 * 3.4.	通过token查询用户信息
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);
	
	/**
	 * 根据token删除redis中的key。
	 * @param token
	 * @return
	 */
	public TaotaoResult logout(String token);
}
