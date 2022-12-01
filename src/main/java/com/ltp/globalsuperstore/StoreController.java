
package com.ltp.globalsuperstore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




@Controller
public class StoreController {
    
    private List<Item> items = new ArrayList<Item>();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false)String id){

        model.addAttribute("categories", Constants.CATEGORIES);
        System.out.println("id: " + id);
        int index =  getIndex(id);
        // sending model value if an update request param was passed or not
        model.addAttribute("item", index != 1000 ? items.get(index) : new Item());


        return "form";
    };

     @PostMapping("/submitItem")
    public String handleSubmit(Item item, RedirectAttributes redirectAttributes) {

        int index = getIndex(item.getId());

        String status = Constants.SUCCESS_STATUS;

        if(index == 1000){
            items.add(item);
        } else if (within5Days(item.getDate(), items.get(index).getDate())){
            items.set(index, item);
        }else {
            status = Constants.FAILED_STATUS;
        }
        
        
        redirectAttributes.addFlashAttribute("status", status);

        return"redirect:inventory";

    }

       @GetMapping("/inventory")
    public String getInventory(Model model){
        model.addAttribute("items", items);
        return "inventory";
    }

    public int getIndex (String id){
        for(int i = 0; i < items.size(); i++ ){
            if(items.get(i).getId().equals(id)){
                return i;
            }
        }
        return Constants.NOMATCH;
    }

     public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }
    
}
