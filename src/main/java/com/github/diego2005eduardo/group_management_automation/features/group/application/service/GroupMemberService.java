package com.github.diego2005eduardo.group_management_automation.features.group.application.service;

import com.github.diego2005eduardo.group_management_automation.features.group.application.dto.MemberDTO;
import com.github.diego2005eduardo.group_management_automation.features.group.infrastructure.service.GoogleGroupsService;
import com.google.api.services.admin.directory.model.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupMemberService {

    private final GoogleGroupsService googleGroupsService;

    public GroupMemberService(GoogleGroupsService googleGroupsService) {
        this.googleGroupsService = googleGroupsService;
    }

    public String addMember(String groupKey, MemberDTO request) {
        Member member = googleGroupsService.createMember(request.getEmail(), request.getRole());
        googleGroupsService.addMemberToGroup(groupKey, member);
        return "Member successfully added.";
    }

    public String removeMember(String groupKey, String email) {
        googleGroupsService.removeMemberFromGroup(groupKey, email);
        return "Member successfully removed.";
    }

    public List<MemberDTO> listAllMembers(String groupKey) {
        List<Member> members = googleGroupsService.listGroupMembers(groupKey);

        return members.stream()
                .map(member -> new MemberDTO(
                        member.getEmail(),
                        member.getRole()
                ))
                .collect(Collectors.toList());
    }

    private boolean memberExists(String groupKey, String email) {
        List<Member> members = googleGroupsService.listGroupMembers(groupKey);
        return members.stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

}