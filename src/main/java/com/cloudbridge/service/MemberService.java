package com.cloudbridge.service;

import com.cloudbridge.dto.MemberDto;
import com.cloudbridge.entity.Member;
import com.cloudbridge.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDto.Response> findAllMembers() {
        return memberRepository.findAll().stream().map(MemberDto.Response::new).toList();
    }
}
