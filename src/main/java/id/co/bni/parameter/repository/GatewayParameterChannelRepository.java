package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.GatewayParameterChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GatewayParameterChannelRepository extends JpaRepository<GatewayParameterChannel, String> {
    @Query("select km from GATEWAY_PARAMETER_CHANNEL km where km.transCode = ?1")
    GatewayParameterChannel findByTransCode(String transCode);
}
