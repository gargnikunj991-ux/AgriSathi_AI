package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FileUploadResponse;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Upload APIs", description = "Endpoints for uploading media files to Cloudinary and fetching user image upload records")
@SecurityRequirement(name = "bearerAuth")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload File", description = "Uploads a document or image file to cloud storage (Cloudinary).")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "File uploaded successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Empty file, invalid format, or file size > 5MB")
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "Multipart file object", required = true) @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.uploadFile(currentUser.getId(), file);
        return new ResponseEntity<>(ApiResponse.success("Image uploaded successfully", response), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Image", description = "Uploads a crop/produce image file (JPG, PNG, WEBP, GIF) to Cloudinary.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid image format or file size > 5MB")
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadImage(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "Image file object", required = true) @RequestParam("image") MultipartFile image) {
        FileUploadResponse response = fileStorageService.uploadImage(currentUser.getId(), image);
        return new ResponseEntity<>(ApiResponse.success("Image uploaded successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get User Uploaded Files", description = "Retrieves all media file upload records for the authenticated farmer.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User files retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> getUserFiles(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<FileUploadResponse> files = fileStorageService.getUserUploadedFiles(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User files retrieved successfully", files));
    }
}
