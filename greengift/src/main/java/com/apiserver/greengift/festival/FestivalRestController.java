package com.apiserver.greengift.festival;

import com.apiserver.greengift._core.security.CustomUserDetails;
import com.apiserver.greengift._core.utils.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/festival")
public class FestivalRestController {

    private final FestivalWriteService festivalService;
    private final FestivalReadService festivalReadService;

    @PostMapping("")
    public ResponseEntity<?> addFestival(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody @Valid FestivalRequest.AddFestival requestDTO) {
        festivalService.addFestival(userDetails.getUser(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/product/{festivalId}")
    public ResponseEntity<?> addProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody @Valid FestivalRequest.AddProduct requestDTO,
                                        @PathVariable @Min(1) Long festivalId) {
        festivalService.addProduct(userDetails.getUser(), requestDTO, festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinFestival(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody @Valid FestivalRequest.JoinFestival requestDTO) {
        festivalService.joinFestival(userDetails.getUser(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/random/{festivalId}")
    public ResponseEntity<?> findRandomResult(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable @Min(1) Long festivalId) {
        festivalService.findRandomResult(userDetails.getUser(), festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/result/{festivalId}")
    public ResponseEntity<?> findFestivalResult(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable @Min(1) Long festivalId) {
        FestivalResponse.FindFestivalResult response = festivalReadService.findFestivalResult(userDetails.getUser(), festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findJoinedFestival(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FestivalResponse.FindJoinedFestival> response = festivalReadService.findJoinedFestival(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/manager")
    public ResponseEntity<?> findFestivalByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FestivalResponse.FindFestivalByUser> response = festivalReadService.findFestivalByUser(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }
}
