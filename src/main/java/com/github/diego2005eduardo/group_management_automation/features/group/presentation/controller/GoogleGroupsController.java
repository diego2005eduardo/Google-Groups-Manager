package com.github.diego2005eduardo.group_management_automation.features.group.presentation.controller;

import com.github.diego2005eduardo.group_management_automation.features.group.application.dto.MemberDTO;
import com.github.diego2005eduardo.group_management_automation.features.group.application.service.GroupMemberService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/google-groups")
public class GoogleGroupsController {

    private final GroupMemberService groupMemberService;

    public GoogleGroupsController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @PostMapping("/{groupKey}/members")
    public ResponseEntity<String> addMemberToGroup(@PathVariable String groupKey, @Valid @RequestBody MemberDTO body) {
        String response = groupMemberService.addMember(groupKey, body);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupKey}/members/{memberEmail}")
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable String groupKey, @PathVariable String memberEmail) {
        String response = groupMemberService.removeMember(groupKey, memberEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupKey}/members")
    public ResponseEntity<List<MemberDTO>> listGroupMembers(@PathVariable String groupKey) {
        List<MemberDTO> members = groupMemberService.listAllMembers(groupKey);
        return ResponseEntity.ok(members);
    }
}