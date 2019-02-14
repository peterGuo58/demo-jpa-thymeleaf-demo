package com.zq.springboot.demojpathymeleafdemo.web;

import com.zq.springboot.demojpathymeleafdemo.model.User;
import com.zq.springboot.demojpathymeleafdemo.param.UserParam;
import com.zq.springboot.demojpathymeleafdemo.repositorys.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String index() {
        return "redirect:/list";
    }

    /*由于spring的RequestParam注解接收的参数是来自于requestHeader中，即请求头，也就是在url中，格式为xxx?username=123&password=456，
    而RequestBody注解接收的参数则是来自于requestBody中，即请求体中。
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findList(pageable);
        model.addAttribute("users", users);
        return "user/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "user/userAdd";
    }

    @RequestMapping("/add")
    public String add(@Valid UserParam userParam, BindingResult result, Model model) {
        String errorMsg = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = errorMsg + error.getCode() + "-" + error.getDefaultMessage() + ";";
            }
            model.addAttribute("errorMsg", errorMsg);
            return "user/userAdd";
        }
        User u = userRepository.findByUserName(userParam.getUserName());
        if (u != null) {
            model.addAttribute("errorMsg", "用户已存在!");
            return "user/userAdd";
        }
        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        user.setRegTime(new Date());
        userRepository.save(user);
        return "redirect:/list";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Model model, Long id) {
        User user = userRepository.findById((long) id);
        model.addAttribute("user", user);
        return "user/userEdit";
    }

    @RequestMapping("/edit")
    public String edit(@Valid UserParam userParam, BindingResult result, Model model) {
        String errorMsg = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = errorMsg + error.getCode() + "-" + error.getDefaultMessage() + ";";
            }
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("user", userParam);
            return "user/userEdit";
        }

        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        user.setRegTime(new Date());
        userRepository.save(user);
        return "redirect:/list";
    }

    @RequestMapping("/delete")
    public String delete(Long id) {
        userRepository.deleteById(id);
        return "redirect:/list";
    }

    /* 另外，还有一种应用场景，接口规范为resultful风格时，举个例子：如果要获取某个id下此条问题答案的查询次数的话，
    则后台就需要动态获取参数，其注解为@PathVariable，并且requestMapping中的value应为value="/{id}/queryNum"，
    * */
   /* @RequestMapping(value = "/{id}/queryNum")
    public String pathvalid(@PathVariable("id") String id) throws Exception{
        System.out.println("更改id为"+id+"的更新次数为:");
        return "";//待续......
    }*/
}
