package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.AdminVO;

public interface AdminService {

    boolean signup(AdminVO admin);

    AdminVO login(AdminVO admin);

    boolean isIdDuplicate(String id);
}
