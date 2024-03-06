package controller;

import java.io.IOException;

import eriq.flamework.annotations.Controller;
import eriq.flamework.annotations.Singleton;
import eriq.flamework.annotations.URLMapping;
import eriq.flamework.model.ModelView;
import eriq.flamework.servlet.ServletEntity;

@Controller
@Singleton
public class HelloController {
    @URLMapping("hello.do")
    public ModelView hello(ServletEntity entity) throws IOException{
        ModelView view=new ModelView();
        view.setView("pages/layout/layout.jsp");
        view.addItem("title", "Welcome");
        view.addItem("viewpage", "hello.jsp");
        return view;
    }
}
