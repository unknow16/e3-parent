package com.fuyi.e3.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fuyi.e3.common.vo.FastDFSClient;

@Controller
public class PictureController {

	@RequestMapping("/pic/upload")
	@ResponseBody
	public Map fileUpload(MultipartFile uploadFile) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//1.取文件的扩展名
		String originalFilename = uploadFile.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		
		//2.创建一个FastDFS的客户端
		//FastDFSClient fastDFSClient = new FastDFSClient(conf);
		return null;
	}
}
