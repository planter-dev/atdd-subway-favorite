package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final StationService stationService;

  public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
    this.favoriteRepository = favoriteRepository;
    this.stationService = stationService;
  }

  @Transactional
  public Long saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
    if (Objects.equals(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
      throw new IllegalArgumentException();
    }

    Station source = stationService.findById(favoriteRequest.getSource());
    Station target = stationService.findById(favoriteRequest.getTarget());
    Favorite favorite = new Favorite(memberId, source, target);
    favoriteRepository.save(favorite);
    return favorite.getId();
  }

  public List<FavoriteResponse> searchFavorites(Long memberId) {
    return favoriteRepository.findByMemberId(memberId)
      .stream()
      .map(FavoriteResponse::of)
      .collect(Collectors.toList());
  }

  @Transactional
  public void deleteFavorite(Long id) {
    favoriteRepository.deleteById(id);
  }
}
