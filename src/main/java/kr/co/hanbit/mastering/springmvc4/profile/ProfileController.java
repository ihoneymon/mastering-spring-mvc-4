package kr.co.hanbit.mastering.springmvc4.profile;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.hanbit.mastering.springmvc4.date.KRLocalDateFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProfileController {

    private UserProfileSession userProfileSession;

    @Autowired
    public ProfileController(UserProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @ModelAttribute
    public ProfileForm getProfileForm() {
        return userProfileSession.toForm();
    }

    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {
        return KRLocalDateFormatter.getPattern(locale);
    }

    @RequestMapping(value = "/profile", params = { "save" }, method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/profile-page";
        }
        log.debug("ProfileForm: {}", profileForm);
        userProfileSession.saveForm(profileForm);
        
        return "redirect:/search/mixed;keywords=" + String.join(",", profileForm.getTastes());
    }

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm) {
        return "profile/profile-page";
    }

    @RequestMapping(value = "/profile", params = { "addTaste" })
    public String addRow(ProfileForm profileForm) {
        profileForm.getTastes().add(null);
        return "profile/profile-page";
    }

    @RequestMapping(value = "/profile", params = { "removeTaste" })
    public String removeRow(ProfileForm profileForm, HttpServletRequest req) {
        Integer rowId = Integer.valueOf(req.getParameter("removeTaste"));
        profileForm.getTastes().remove(rowId.intValue());
        return "profile/profile-page";
    }

}
