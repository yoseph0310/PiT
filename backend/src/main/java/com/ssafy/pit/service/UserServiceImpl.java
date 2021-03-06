package com.ssafy.pit.service;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ssafy.pit.entity.User;
import com.ssafy.pit.repository.CodeRepositorySupport;
import com.ssafy.pit.repository.UserRepository;
import com.ssafy.pit.request.UserInfoPutReq;
import com.ssafy.pit.request.UserRegisterPostReq;
import com.ssafy.pit.response.UserInfoGetRes;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CodeRepositorySupport codeRepositorySupport;
	
	// 이미지 생성폴더 이름
	String uploadFolder = "upload";
	
	String uploadPath = "/home" + File.separator + "ubuntu" + File.separator + "S05P13A204"
            + File.separator + "backend" 
            + File.separator + "src" 
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "static";
	
	@Override
	public User registerUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		
		user.setUserGender(userRegisterInfo.getUserGender());
		user.setUserName(userRegisterInfo.getUserName());
		user.setUserEmail(userRegisterInfo.getUserEmail());
		user.setUserPwd(passwordEncoder.encode(userRegisterInfo.getUserPwd()));
		user.setUserType(userRegisterInfo.getUserType());
		user.setUserProfile(userRegisterInfo.getUserProfile());
		user.setUserNickname(userRegisterInfo.getUserNickname());
		user.setUserDesc(userRegisterInfo.getUserDesc());
		user.setUserPhone(userRegisterInfo.getUserPhone());
		
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserEmail(String userEmail) {
		User user = userRepository.findUserByUserEmail(userEmail);
		return user;
	}
	
	
	@Override
	public User getUserByUserNickname(String userNickname) {
		User user = userRepository.findUserByUserNickname(userNickname);
		return user;
	}

	@Override
	public UserInfoGetRes getUserInfo(User user) {
		UserInfoGetRes userInfo = new UserInfoGetRes();
		BeanUtils.copyProperties(user, userInfo);
		String userGenderName = codeRepositorySupport.getCodeName("001", user.getUserGender());
		String userTypeName = codeRepositorySupport.getCodeName("002", user.getUserType());
		userInfo.setUserGenderName(userGenderName);
		userInfo.setUserTypeName(userTypeName);
		return userInfo;
	}
	
	@Override
	public int update(User user, MultipartHttpServletRequest request) {
		try {
			String deleteFileUrl = user.getUserProfile();
			File uploadDir = new File(uploadPath + File.separator + uploadFolder);
			if(!uploadDir.exists()) uploadDir.mkdir();
			
			File file = null;
			if(deleteFileUrl != null) {
				file = new File(uploadPath + File.pathSeparator, deleteFileUrl);
				
				if(file.exists()) {
					file.delete();
				}
			}
			
			MultipartFile part= request.getFile("file");
			if (part == null) {
				String fileUrl = "";
				user.setUserProfile(fileUrl);
				userRepository.save(user);
				return 2;
			}
			else {				
				String fileName = part.getOriginalFilename();
				UUID uuid = UUID.randomUUID();
				String extension = FilenameUtils.getExtension(fileName);
				String savingFileName = uuid + "." + extension;
				File destFile = new File(uploadPath + File.separator + uploadFolder + File.separator + savingFileName);
				part.transferTo(destFile);
				String fileUrl = "https://i5a204.p.ssafy.io:8080/static/"+uploadFolder + "/" + savingFileName;
				
				System.out.println("fileUrl : "+ fileUrl);
				user.setUserProfile(fileUrl);
				userRepository.save(user);
				return 1;
			}
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	
	@Override
	public int update(User user, UserInfoPutReq userUpdateInfo) {
		try {
			String userNickname = userUpdateInfo.getUserNickname();
			if(userRepository.findUserByUserNickname(userNickname) != null && 
					!user.getUserNickname().equals(userUpdateInfo.getUserNickname())) {
				return 2;
			};
			
			userUpdateInfo.setUserPwd(passwordEncoder.encode(userUpdateInfo.getUserPwd()));
			BeanUtils.copyProperties(userUpdateInfo, user);
			userRepository.save(user);
			return 1;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}

	@Override
	public int delete(String userEmail) {
		String deleteFileUrl = userRepository.findUserByUserEmail(userEmail).getUserProfile();
		File file = null;
        if(deleteFileUrl != null) {
           file = new File(uploadPath + File.separator, deleteFileUrl);
           if(file.exists()) {
              file.delete();
           }
        }
		
		return userRepository.deleteByUserEmail(userEmail);
	}

	@Override
	public int validateUserType(String userEmail) {
		User user = userRepository.findUserByUserEmail(userEmail);
		if (user.getUserType().equals("001")) {
			return 1;
		}
		else if (user.getUserType().equals("002")) {
			return 2;
		}
		else {
			return 3;
		}
	}
	
}
