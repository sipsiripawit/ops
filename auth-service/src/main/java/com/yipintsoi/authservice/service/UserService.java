package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.dto.UserDTO;

/**
 * Service สำหรับจัดการข้อมูลผู้ใช้
 */
public interface UserService {

    /**
     * ดึงข้อมูลผู้ใช้ตาม ID
     * @param id ID ของผู้ใช้
     * @return ข้อมูลผู้ใช้
     */
    UserDTO getUserById(Long id);

    /**
     * ดึงข้อมูลผู้ใช้ตาม username
     * @param username ชื่อผู้ใช้
     * @return ข้อมูลผู้ใช้
     */
    UserDTO getUserByUsername(String username);
}