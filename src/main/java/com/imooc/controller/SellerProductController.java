package com.imooc.controller;

import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.exception.ProductException;
import com.imooc.form.ProductForm;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        //接口从第1页开始, 查询从第0页开始
        PageRequest request = PageRequest.of(page-1, size);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("product/list", map);
    }

    @RequestMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String, Object> map) {
        try {
            productService.onSale(productId);
        } catch (ProductException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/product2/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/product2/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                                Map<String, Object> map) {
        try {
            productService.offSale(productId);
        } catch (ProductException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/product2/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/product2/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/index")
    // productId非必须
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                              Map<String, Object> map) {
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        }
        //查询所有的类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("product/index", map);
    }

    @PostMapping("/save")
    //相对cacheable, 每次都会执行然后把结果写入Redis
    // @CachePut(cacheNames = "product", key = "123")
    // 清除缓存
    @CacheEvict(cacheNames = "product", key = "124")
    public ModelAndView save(@Valid ProductForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/product2/seller/product/index");
            return new ModelAndView("common/error", map);
        }
        try {
            ProductInfo productInfo = new ProductInfo();
            if (!StringUtils.isEmpty(form.getProductId())){
                productInfo = productService.findOne(form.getProductId());
            } else {
                form.setProductId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(form, productInfo);
            productService.save(productInfo);
        } catch (ProductException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/product2/seller/product/index");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/product2/seller/product/list");
        return new ModelAndView("common/success", map);
    }
}
