package com.project.pickyou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SampleController {

    @RequestMapping("/content")
    public String content(){
        return "design/content";
    }

    @RequestMapping("/index")
    public String index(){
        return "sample/index";
    }
    @RequestMapping("/forgot")
    public String forgot(){
        return "sample/auth-forgot-password-basic";
    }
    @RequestMapping("/samlogin")
    public String samlogin(){
        return "sample/auth-login-basic";
    }
    @RequestMapping("/register")
    public String register(){
        return "sample/auth-register-basic";
    }
    @RequestMapping("/cards")
    public String cards(){
        return "sample/cards-basic";
    }
    @RequestMapping("/perfectscrollbar")
    public String perfectscrollbar(){
        return "sample/extended-ui-perfect-scrollbar";
    }
    @RequestMapping("/divider")
    public String divider(){
        return "sample/extended-ui-text-divider";
    }
    @RequestMapping("/horizontal")
    public String horizontal(){
        return "sample/form-layouts-horizontal";
    }
    @RequestMapping("/vertical")
    public String vertical(){
        return "sample/form-layouts-vertical";
    }
    @RequestMapping("/inputs")
    public String inputs(){
        return "sample/forms-basic-inputs";
    }
    @RequestMapping("/inputGroups")
    public String inputGroups(){
        return "sample/forms-input-groups";
    }
    @RequestMapping("/boxicons")
    public String boxicons(){
        return "sample/icons-boxicons";
    }
    @RequestMapping("/blank")
    public String blank(){
        return "sample/layouts-blank";
    }
    @RequestMapping("/scrollbar")
    public String container(){
        return "sample/layouts-container";
    }
    @RequestMapping("/fluid")
    public String fluid(){
        return "sample/layouts-fluid";
    }
    @RequestMapping("/menu")
    public String menu(){
        return "sample/layouts-without-menu";
    }
    @RequestMapping("/withoutNavbar")
    public String withoutNavbar(){
        return "sample/layouts-without-navbar";
    }
    @RequestMapping("/account")
    public String account(){
        return "sample/pages-account-settings-account";
    }
    @RequestMapping("/connections")
    public String connections(){
        return "sample/pages-account-settings-connections";
    }
    @RequestMapping("/notifications")
    public String notifications(){
        return "sample/pages-account-settings-notifications";
    }
    @RequestMapping("/withoutMenu")
    public String withoutMenu(){
        return "sample/layouts-without-menu";
    }
    @RequestMapping("/maintenance")
    public String maintenance(){
        return "sample/pages-misc-under-maintenance";
    }
    @RequestMapping("/tables")
    public String tables(){
        return "sample/tables-basic";
    }
    @RequestMapping("/typography")
    public String typography(){
        return "sample/ui-typography";
    }

    @RequestMapping("/popovers")
    public String popovers(){
        return "sample/ui-tooltips-popovers";
    }

    @RequestMapping("/toasts")
    public String toasts(){
        return "sample/ui-toasts";
    }

    @RequestMapping("/tabsPills")
    public String tabsPills(){
        return "sample/ui-tabs-pills";
    }

    @RequestMapping("/spinners")
    public String spinners(){
        return "sample/ui-spinners";
    }

    @RequestMapping("/progress")
    public String progress(){
        return "sample/ui-progress";
    }

    @RequestMapping("/breadcrumbs")
    public String breadcrumbs(){
        return "sample/ui-pagination-breadcrumbs";
    }

    @RequestMapping("/offcanvas")
    public String offcanvas(){
        return "sample/ui-offcanvas";
    }

    @RequestMapping("/navbar")
    public String navbar(){
        return "sample/ui-navbar";
    }

    @RequestMapping("/modals")
    public String modals(){
        return "sample/ui-modals";
    }

    @RequestMapping("/groups")
    public String groups(){
        return "sample/ui-list-groups";
    }

    @RequestMapping("/footer")
    public String footer(){
        return "sample/ui-footer";
    }

    @RequestMapping("/dropdowns")
    public String dropdowns(){
        return "sample/ui-dropdowns";
    }

    @RequestMapping("/collapse")
    public String collapse(){
        return "sample/ui-collapse";
    }

    @RequestMapping("/carousel")  //이거 메인어덤
    public String carousel(){
        return "sample/ui-carousel";
    }

    @RequestMapping("/buttons")
    public String buttons(){
        return "sample/ui-buttons";
    }

    @RequestMapping("/badges")
    public String badges(){
        return "sample/ui-badges";
    }

    @RequestMapping("/alerts")
    public String alerts(){
        return "sample/ui-alerts";
    }

    @RequestMapping("/accordion")
    public String accordion(){
        return "sample/ui-accordion";
    }

    @RequestMapping("/index2")
    public String index2(){
        return "sample2/index";
    }

    @RequestMapping("/designContent")
    public String index3(){
        return "design/content";
    }

}
