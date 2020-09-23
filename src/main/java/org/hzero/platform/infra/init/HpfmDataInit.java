package org.hzero.platform.infra.init;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.BooleanUtils;
import org.hzero.boot.platform.encrypt.EncryptRepository;
import org.hzero.core.message.MessageAccessor;
import org.hzero.platform.app.service.DatabaseService;
import org.hzero.platform.app.service.DatasourceInfoService;
import org.hzero.platform.domain.repository.ConfigRepository;
import org.hzero.platform.domain.repository.PermissionRangeRepository;
import org.hzero.platform.domain.repository.ProfileValueRepository;
import org.hzero.platform.domain.repository.ResponseMessageRepository;
import org.hzero.platform.domain.service.CodeRuleDomainService;
import org.hzero.platform.domain.service.PromptDomainService;
import org.hzero.platform.infra.properties.PlatformProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.choerodon.mybatis.pagehelper.Dialect;

/**
 * <p>
 * 应用启动时缓存初始化数据
 * </p>
 *
 * @author yunxiang.zhou01@hand-china.com 2018/06/11 19:15
 */
@Component
public class HpfmDataInit implements SmartInitializingSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(HpfmDataInit.class);

    private final PlatformProperties platformProperties;

    private final ProfileValueRepository profileValueRepository;

    private final ConfigRepository configRepository;

    private final PromptDomainService promptDomainService;

    private final CodeRuleDomainService codeRuleDomainService;

    private final PermissionRangeRepository permissionRangeRepository;

    private final ResponseMessageRepository responseMessageRepository;

    private final DatabaseService databaseService;

    private final DatasourceInfoService datasourceRelService;

    private final EncryptRepository encryptRepositoy;

    @Autowired
    public HpfmDataInit(ProfileValueRepository profileValueRepository, ConfigRepository configRepository,
                        PromptDomainService promptDomainService, CodeRuleDomainService codeRuleDomainService,
                        PermissionRangeRepository permissionRangeRepository, DatabaseService databaseService,
                        DatasourceInfoService datasourceRelService, PlatformProperties platformProperties,
                        EncryptRepository encryptRepositoy, ResponseMessageRepository responseMessageRepository,
                        Dialect dialect) {
        this.profileValueRepository = profileValueRepository;
        this.configRepository = configRepository;
        this.promptDomainService = promptDomainService;
        this.codeRuleDomainService = codeRuleDomainService;
        this.permissionRangeRepository = permissionRangeRepository;
        this.databaseService = databaseService;
        this.datasourceRelService = datasourceRelService;
        this.platformProperties = platformProperties;
        this.encryptRepositoy = encryptRepositoy;
        this.responseMessageRepository = responseMessageRepository;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 加入消息文件
        MessageAccessor.addBasenames("classpath:messages/messages_hpfm");

        LOGGER.info("Start init redis cache.");

        // 缓存公钥和私钥
        cacheEncryptKey();

        if (!BooleanUtils.isTrue(platformProperties.getInitCache())) {
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(12, new ThreadFactoryBuilder().setNameFormat("HpfmDataInit-%d").build());

        executorService.submit(profileValueRepository::initAllProfileValueToRedis);
        executorService.submit(configRepository::initAllConfigToRedis);
        executorService.submit(() -> promptDomainService.initAllPromptCacheValue(true));
        executorService.submit(responseMessageRepository::initAllReturnMessageToRedis);
        executorService.submit(codeRuleDomainService::initCodeRuleCache);
        executorService.submit(permissionRangeRepository::initAllData);
        executorService.submit(databaseService::initAllData);
        executorService.submit(datasourceRelService::initAllData);

        executorService.shutdown();

        LOGGER.info("Finish init redis cache.");
    }

    /**
     * 缓存加密的私钥和公钥
     */
    private void cacheEncryptKey() {
        PlatformProperties.Encrypt encrypt = platformProperties.getEncrypt();
        encryptRepositoy.savePublicKey(encrypt.getPublicKey());
        encryptRepositoy.savePrivateKey(encrypt.getPrivateKey());
        LOGGER.info("Init encrypt publicKey and privateKey. \n publicKey is [{}] \n privateKey is [{}]", encrypt.getPublicKey(), encrypt.getPrivateKey());
    }
}
