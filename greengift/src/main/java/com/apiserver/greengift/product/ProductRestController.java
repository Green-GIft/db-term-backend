package com.apiserver.greengift.product;

import com.apiserver.greengift._core.security.CustomUserDetails;
import com.apiserver.greengift._core.utils.ApiUtils;
import com.apiserver.greengift.festival.FestivalRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRestController {

    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;

    @PostMapping("/{festivalId}")
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody @Valid FestivalRequest.AddProduct requestDTO,
                                        @PathVariable @Min(1) Long festivalId) {
        productWriteService.addProduct(userDetails.getUser(), requestDTO, festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/amount/{productId}")
    public ResponseEntity<?> addAmount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid ProductRequest.AddProductAmount requestDTO,
                                       @PathVariable @Min(1) Long productId) {
        productWriteService.addAmount(userDetails.getUser(), requestDTO, productId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/buy/{productId}")
    public ResponseEntity<?> buyProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable @Min(1) Long productId) {
        productWriteService.buyProduct(userDetails.getUser(), productId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllProduct(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProductResponse.FindProductAll> response = productReadService.findAllProduct(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/participant")
    public ResponseEntity<?> findProductByParticipant(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProductResponse.FindProductByParticipant> response = productReadService.findProductByParticipant(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/manager")
    public ResponseEntity<?> findProductByFestivalManager(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProductResponse.FindProductByFestivalManager> response = productReadService.findProductByFestivalManager(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

}
