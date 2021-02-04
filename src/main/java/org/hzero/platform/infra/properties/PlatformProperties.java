package org.hzero.platform.infra.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HPFM Properties
 *
 * @author gaokuo.dai@hand-china.com 2018年8月20日下午4:24:58
 */
@ConfigurationProperties(prefix = PlatformProperties.PREFIX)
public class PlatformProperties {

    public static final String PREFIX = "hzero.platform";
    private static final String HTTP_PROTOCOL_SUFFIX = "://";

    /**
     * 启动时是否刷新缓存,默认true
     */
    private Boolean initCache = true;
    /**
     * 平台Http协议,默认http
     */
    private String httpProtocol = "http";
    /**
     * 完整平台Http协议,默认http://
     */
    private String fullHttpProtocol = "http://";

    /**
     * @return 启动时是否刷新缓存,默认true
     */
    public Boolean getInitCache() {
        return initCache;
    }

    /**
     * 获取卡片使用的模板角色Code
     */
    private List<String> roleTemplateCodes;

    private Encrypt encrypt = new Encrypt();

    public List<String> getRoleTemplateCodes() {
        return roleTemplateCodes;
    }

    public void setRoleTemplateCodes(List<String> roleTemplateCodes) {
        this.roleTemplateCodes = roleTemplateCodes;
    }

    public void setInitCache(Boolean initCache) {
        this.initCache = initCache;
    }

    /**
     * @return 平台Http协议,默认http
     */
    public String getHttpProtocol() {
        return httpProtocol;
    }

    public void setHttpProtocol(String httpProtocol) {
        this.httpProtocol = httpProtocol;
        this.fullHttpProtocol = httpProtocol + HTTP_PROTOCOL_SUFFIX;
    }

    /**
     * @return 完整平台Http协议,默认http://
     */
    public String getFullHttpProtocol() {
        return fullHttpProtocol;
    }

    public Encrypt getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Encrypt encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * 加密私钥和公钥
     */
    public static class Encrypt {
        /**
         * 密码加密公钥
         */
        private String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJL0JkqsUoK6kt3JyogsgqNp9VDGDp+t3ZAGMbVoMPdHNT2nfiIVh9ZMNHF7g2XiAa8O8AQWyh2PjMR0NiUSVQMCAwEAAQ==";
        /**
         * 密码加密私钥
         */
        private String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAkvQmSqxSgrqS3cnKiCyCo2n1UMYOn63dkAYxtWgw90c1Pad+IhWH1kw0cXuDZeIBrw7wBBbKHY+MxHQ2JRJVAwIDAQABAkB3TKXZcVP6tSSN0UgOjLPxng99Z4xvrWJ1jdHFB7TYyUDcnOtE6GkeeMGxtszYtuZ0m5rN9r8eRvIdVUciXbQhAiEA7WGcLd+cvHHA8xNFrbT78Aq4iYm0TVTS6m05ZEOS328CIQCeetcFV8Kdb+P2Y+yWvMtbyFGhROQIFizvxM3S3TiZrQIhAJLrYPB7f9SaSyOm/+89Htk4qXJmyjM6lrgFFgpaUGL9AiB+pkCr/mSDGOYfA+AQ3rPNl5rUvI9XfxFOVAMAntYayQIhAO0hrnwtng6Ubs+58BN69vAl+sd/xugf0jsHcNgZppAb";


        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

    }

}
