package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.ChannelParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelParameterRepo extends JpaRepository<ChannelParameter, String> {
    @Query("select km from CHANNEL_PARAMETER km where km.channelId = ?1")
    ChannelParameter findByChannelId(String channelId);
}
