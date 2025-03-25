package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.dtos.FriendStatus;
import org.server.socialnetworkserver.dtos.OnlineFriendsDto;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.FollowRepository;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.responses.BasicResponse;
import org.server.socialnetworkserver.responses.OnlineFriendsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OnlineFriendsService {

    private final Map<Long, SseEmitter> userEmitters;
    private final Map<Long, User> connectedUsers;
    private final Map<Long, List<Long>> followingCache;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public OnlineFriendsService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userEmitters = new ConcurrentHashMap<>();
        this.connectedUsers = new ConcurrentHashMap<>();
        this.followingCache  = new ConcurrentHashMap<>();
    }

    public SseEmitter connect(User user) {

        if (userEmitters.containsKey(user.getId())) {
            System.out.println("User already connected. Closing old connection for: " + user.getUsername());
            cleanup(user.getId());
        }

        if (!followingCache.containsKey(user.getId())) {
            List<Long> followingIds = followRepository.findFollowingIdsByUserId(user.getId());
            followingCache.put(user.getId(), followingIds);
        }


        SseEmitter emitter = new SseEmitter(2 * 60 * 1000L); // 2 דקות
        userEmitters.put(user.getId(), emitter);
        connectedUsers.put(user.getId(), user);

        emitter.onCompletion(() -> cleanup(user.getId()));
        emitter.onTimeout(() -> cleanup(user.getId()));
        emitter.onError((throwable) -> cleanup(user.getId()));

        notifyFriends(user.getId(), true);
        return emitter;
    }



    public BasicResponse disconnect(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            cleanup(user.getId());
            return new BasicResponse(true,"Disconnected");
        }
        return new BasicResponse(false,"User not found");
    }

    private void cleanup(Long userId) {
        SseEmitter emitter = userEmitters.remove(userId);
        if (emitter != null) {
            emitter.complete();
        }
        connectedUsers.remove(userId);
        followingCache.remove(userId);
        System.out.println("Cleaned up emitter for userId: " + userId);
        notifyFriends(userId, false);
    }

    private void notifyFriends(long userId, boolean online) {
        List<Long> followingIds = followingCache.get(userId);

        if (followingIds == null) return;

        User user = connectedUsers.get(userId);
        if (user == null) return;

        FriendStatus status = new FriendStatus(userId, user.getUsername(), user.getProfilePicture(), online);

        for (Long friendId : followingIds) {
            SseEmitter emitter = userEmitters.get(friendId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("FRIEND_STATUS").data(status));
                } catch (IOException e) {
                    cleanup(friendId);
                }
            }
        }
    }

    public OnlineFriendsResponse getInitialOnlineFriends(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null){
            return new OnlineFriendsResponse(false, "User not found");
        }

        List<OnlineFriendsDto> followingDtos = followRepository.findFollowingUsersByUserId(user.getId());

        List<OnlineFriendsDto> onlineFriendsDtos = followingDtos.stream()
                .filter(friend -> userEmitters.containsKey(friend.getId()))
                .collect(Collectors.toList());

        return new OnlineFriendsResponse(true, "All online friends sent.", onlineFriendsDtos);
    }
}