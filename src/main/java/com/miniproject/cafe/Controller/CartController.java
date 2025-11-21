package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.CartService;
import com.miniproject.cafe.VO.CartItemVO;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class CartController {

    @Autowired
    private CartService cartService;

    // HomeController와 동일한 로그인 체크 메서드
    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }

    private String getMemberId(Authentication auth) {
        return isLoggedIn(auth) ? auth.getName() : null;
    }

    @GetMapping("/cart")
    public String cartPage(Authentication auth, Model model, HttpSession session) {
        boolean isLoggedIn = isLoggedIn(auth);
        model.addAttribute("IS_LOGGED_IN", isLoggedIn);

        // HomeController와 동일한 방식으로 로그인 체크
        if (!isLoggedIn(auth)) {
            return "redirect:/home/";
        }

        String currentStore = (String) session.getAttribute("storeName");
        model.addAttribute("storeName", currentStore);

        String memberId = getMemberId(auth);
        Map<String, Object> cartData;

        try {
            cartData = cartService.getCartList(memberId);

            if (cartData != null && cartData.get("cartItems") != null) {
                List<Map<String, Object>> cartItems = (List<Map<String, Object>>) cartData.get("cartItems");
                List<Map<String, Object>> validItems = new ArrayList<>();

                for (Map<String, Object> item : cartItems) {
                    if (item.get("MENU_PRICE") != null &&
                            Integer.parseInt(item.get("MENU_PRICE").toString()) > 0) {
                        validItems.add(item);
                    }
                }
                cartData.put("cartItems", validItems);
            }

            if (cartData == null) {
                cartData = new HashMap<>();
                cartData.put("cartItems", new ArrayList<>());
                cartData.put("totalPrice", 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            cartData = new HashMap<>();
            cartData.put("cartItems", new ArrayList<>());
            cartData.put("totalPrice", 0);
        }

        model.addAttribute("cartItems", cartData.get("cartItems"));
        model.addAttribute("totalPrice", cartData.get("totalPrice"));
        model.addAttribute("memberId", memberId);

        return "cart";
    }

    @GetMapping("/cart/list/{memberId}")
    @ResponseBody
    public Map<String, Object> getCartList(@PathVariable String memberId) {
        return cartService.getCartList(memberId);
    }

    @PostMapping("/cart/items")
    @ResponseBody
    public int addCartItem(@RequestBody CartItemVO cartItemVO) {
        return cartService.addCartItem(cartItemVO);
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@PathVariable long cartItemId) {
        try {
            int result = cartService.deleteCartItem(cartItemId);
            if (result == 1) {
                return ResponseEntity.ok().body("delete success");
            }
            return ResponseEntity.badRequest().body("delete fail");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("delete error");
        }
    }

    @PatchMapping("/cart/items/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> changeQuantityCartItem(@PathVariable long cartItemId,
                                                         @RequestParam int quantity) {
        int result = cartService.changeQuantityCartItem(cartItemId, quantity);
        if (result == 1) {
            return ResponseEntity.ok().body("change success");
        }
        return ResponseEntity.badRequest().body("change fail");
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestBody Map<String, Object> cartData,
                                         Authentication auth) {
        Map<String, Object> result = new HashMap<>();

        // HomeController 방식으로 체크
        if (!isLoggedIn(auth)) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        try {
            String memberId = getMemberId(auth);
            String menuId = (String) cartData.get("menuId");
            int quantity = Integer.parseInt(cartData.get("quantity").toString());
            String temp = (String) cartData.get("temp");
            boolean tumblerUse = Boolean.parseBoolean(cartData.get("tumblerUse").toString());
            int shotCount = Integer.parseInt(cartData.get("shotCount").toString());
            int vanillaSyrupCount = Integer.parseInt(cartData.get("vanillaSyrupCount").toString());
            int whippedCreamCount = Integer.parseInt(cartData.get("whippedCreamCount").toString());

            int addResult = cartService.addToCart(memberId, menuId, quantity, temp,
                    tumblerUse, shotCount, vanillaSyrupCount, whippedCreamCount);

            if (addResult > 0) {
                result.put("success", true);
                result.put("message", "장바구니에 추가되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "장바구니 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 오류가 발생했습니다.");
        }

        return result;
    }
}