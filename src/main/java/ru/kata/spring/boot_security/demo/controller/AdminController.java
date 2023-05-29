package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController (UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/new_user")
    public String createUser(@ModelAttribute("emptyUser") User user,
                             @RequestParam(value = "checkedRoles") String[] selectResult) {
        for (String s : selectResult) {
            user.addRole(roleService.getRoleByName("ROLE_" + s));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping
    public String getAllUsers(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        List<Role> allRoles = roleService.findAllRoles();
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("User", user);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allPossibleRoles", allRoles);
        model.addAttribute("emptyUser", new User());
        return "/admin";
    }

    @GetMapping("/edit/{id}")
    public String updateUser(@ModelAttribute User user, @PathVariable("id") Long id,
                             @RequestParam(value = "userRolesSelector")
                             String[] selectResult) {
        for (String s : selectResult) {
            user.addRole(roleService.getRoleByName("ROLE_" + s));
        }
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserSubmit(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
