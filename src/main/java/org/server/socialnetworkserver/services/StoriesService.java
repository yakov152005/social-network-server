package org.server.socialnetworkserver.services;


import org.server.socialnetworkserver.dtos.StoriesDto;
import org.server.socialnetworkserver.entitys.Stories;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.StoriesRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.StoriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.server.socialnetworkserver.utils.UploadFileToCloud.uploadFileToCloud;

@Service
public class StoriesService {

    private final UserRepository userRepository;
    private final StoriesRepository storiesRepository;


    @Autowired
    public StoriesService(UserRepository userRepository, StoriesRepository storiesRepository) {
        this.userRepository = userRepository;
        this.storiesRepository = storiesRepository;
    }

    public BasicResponse addStories(String username, MultipartFile postImageFile, String postImageUrl) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new BasicResponse(false, "User not found.");
        }

        if (postImageFile == null && postImageUrl == null) {
            return new BasicResponse(false, "Please enter the url or file");
        }

        if (user.getStories().isEmpty()) {

            try {
                String finalPostPicUrl;
                if (postImageFile != null && !postImageFile.isEmpty()) {
                    finalPostPicUrl = uploadFileToCloud(postImageFile);
                } else if (postImageUrl != null && postImageUrl.startsWith("http")) {
                    finalPostPicUrl = postImageUrl;
                } else {
                    return new BasicResponse(false, "Invalid stories picture format.");
                }
                Stories newStories = new Stories(user, finalPostPicUrl);
                storiesRepository.save(newStories);

                return new BasicResponse(true, "Stories added successfully");
            } catch (IOException e) {
                return new BasicResponse(false, "Error uploading stories picture.");
            }
        }
        return new BasicResponse(false, "You already have a story. Please wait 24 hours before uploading a new one.");
    }

    public StoriesResponse getStoriesForUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            return new StoriesResponse(false, "User not found.", null);
        }

        List<StoriesDto> storiesDto = storiesRepository.findStoriesOfUsersIFollow(username);

        if (storiesDto.isEmpty()) {
            return new StoriesResponse(false, "No stories available.", null);
        }

        return new StoriesResponse(true, "All stories sent.", storiesDto);
    }


}
