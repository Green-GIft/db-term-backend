package com.apiserver.greengift.trash;

import com.apiserver.greengift._core.security.CustomUserDetails;
import com.apiserver.greengift._core.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trash")
public class TrashRestController {

    private final TrashWriteService trashWriteService;
    private final TrashReadService trashReadService;

    @PostMapping("/certificate/{festivalId}")
    public ResponseEntity<?> certificate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody @Valid TrashRequest.CertificateUser requestDTO,
                                         @PathVariable Long festivalId) {
        trashWriteService.certificate(userDetails.getUser(), requestDTO, festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/{festivalId}")
    public ResponseEntity<?> addTrash(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestBody @Valid TrashRequest.AddTrash requestDTO,
                                      @PathVariable Long festivalId) {
        trashWriteService.addTrash(userDetails.getUser(), requestDTO, festivalId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/{trashId}")
    public ResponseEntity<?> deleteTrash(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long trashId) {
        trashWriteService.deleteTrash(userDetails.getUser(), trashId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> findTrashByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TrashResponse.FindTrashByUser> response = trashReadService.findTrashByUser(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/manager")
    public ResponseEntity<?> findBestTrashManager(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TrashResponse.FindBestTrashManager> response = trashReadService.findBestTrashManager(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

}
