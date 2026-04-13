package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.infra.utils.Coordinate;
import com.matheustorres.gympass.infra.utils.GeoUtils;
import com.matheustorres.gympass.web.dtos.request.NearbyGymsRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchNearbyGymsUseCase {

    private final GymRepository gymRepository;
    private static final double NEARBY_DISTANCE_LIMIT_KILOMETERS = 10.0;

    @Transactional(readOnly = true)
    public List<Gym> execute(NearbyGymsRequestDTO dto) {
        Coordinate userCoordinate = new Coordinate(dto.latitude(), dto.longitude());

        return gymRepository.findAll().stream()
                .filter(gym -> {
                    Coordinate gymCoordinate = new Coordinate(
                            gym.getLatitude().doubleValue(),
                            gym.getLongitude().doubleValue()
                    );
                    return GeoUtils.getDistanceBetweenCoordinates(userCoordinate, gymCoordinate) <= NEARBY_DISTANCE_LIMIT_KILOMETERS;
                })
                .collect(Collectors.toList());
    }
}
