package co.nxtgrid.hsm.prism.impl;

import co.nxtgrid.hsm.prism.*;
import co.nxtgrid.hsm.prism.TokenApi.Client;
import co.nxtgrid.token.domain.ManufacturerCode;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


public class PrismHSMConnector {

    public enum CreditTokenType {
        Electricity((short) 0),
        Water((short) 1),
        Gas((short) 2),
        Time((short) 3),
        ElectricityCurrency((short) 4),
        WaterCurrency((short) 5),
        GasCurrency((short) 6),
        TimeCurrency((short) 7);

        private final short subclass;

        CreditTokenType(short subclass) {
            this.subclass = subclass;
        }
    }

    public enum MseToken {
        SetMaximumPowerLimit((short) 0),
        ClearCredit((short) 1),
        SetTariffRate((short) 2),
        ClearTamper((short) 5),
        SetMaximumPhasePowerUnbalanceLimit((short) 6),
        SetWaterMeterFactor((short) 7),
        SetFlag((short) 10);

        private final short subclass;

        MseToken(short subclass) {
            this.subclass = subclass;
        }
    }

    public enum NMseType {
        Primary((long) 0),
        TestLoadSwitch ((long) 1),
        TestInformationDisplay ((long) 2),
        DisplayKWhEnergyTotals ((long) 3),
        DisplayKRN ((long) 4),
        DisplayTI ((long) 5),
        TestTokenReaderDevice ((long) 6),
        DisplayMeterPowerLimit ((long) 7),
        DisplayTamperStatus ((long) 8),
        DisplayPowerConsumption ((long) 9),
        DisplaySoftwareVersion ((long) 10),
        DisplayPhasePowerUnbalanceLimit ((long) 11),
        DisplayWaterMeterFactor ((long) 12),
        DisplayTariffRate ((long) 13),
        DisplayEAValue ((long) 14),
        DisplayNumberKCTsSupported ((long) 15),
        DisplaySGCValue ((long) 16),
        DisplayKENValue ((long) 17),
        DisplayDRNValue ((long) 18),
        InitiateMeterTest ((long) 32);

        private final long control;
        
        NMseType(long control) {
            this.control = control;
        }

        public static NMseType getControl(long value) {
            for (NMseType type : NMseType.values()) {
                if (value == type.control) return type;
            }
            return null;
        }
    }

    public enum FlagTokenType {
        SetFlagCTSTest((short) 0),
        DetectTamper ((short) 1),
        DisconnectService ((short) 2),
        DisconnectOnTamper ((short) 3),
        DisconnectOnPowerLimit ((short) 4),
        DisconnectOnUnderFrequency ((short) 5),
        SetElectricityPaymentMode ((short) 6),
        SetWaterPaymentMode((short) 7),
        SetGasPaymentMode ((short) 8),
        SetTimePaymentMode ((short) 9),
        SetCommissioningMode ((short) 10),
        EnableTIFallbackPowerLimit ((short) 11);

        private final short subclass;

        FlagTokenType(short subclass) {
            this.subclass = subclass;
        }

        public static FlagTokenType getFlagTokenType(short flag) {
            for (FlagTokenType type : FlagTokenType.values()) {
                if (flag == type.subclass) return type;
            }
            return null;
        }
    }

    public enum FlagTokenValue {
        Enable ((short) 1),
        Disable ((short) 0),
        Set ((short) 1),
        Reset ((short) 0),
        EnableReconnectionOfService ((short) 0),
        DisconnectService ((short) 1),
        PostPayment ((short) 0),
        PrePayment ((short) 1),
        SSet ((short) 0),
        Unset ((short) 1);

        private final short subclass;

        FlagTokenValue(short subclass) {
            this.subclass = subclass;
        }

        public static FlagTokenValue getFlagTokenValue(short flag) {
            for (FlagTokenValue value : FlagTokenValue.values()) {
                if (flag == value.subclass) return value;
            }
            return null;
        }
    }

    public String ping(Client client, int sleepMs, String testStr)
        throws TException {
        return client.ping(sleepMs, testStr);
    }

    public List<NodeStatus> getStatus(Client client, String messageID, String accessToken)
        throws TException {
        return client.getStatus(messageID, accessToken);
    }

    public List<Token> fetchTokenResult(Client client, String messageID, String accessToken,
                                 String reqMessageID)
        throws TException {
        return client.fetchTokenResult(messageID, accessToken, reqMessageID);
    }

    public Client getClient(String host, int port)
        throws NoSuchAlgorithmException, IOException,
            TTransportException, KeyManagementException  {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, new SecureRandom());
        SSLSocket socket = (SSLSocket) ctx.getSocketFactory().createSocket(host, port);
        socket.setSoTimeout(0);
        TTransport transport = new TSocket(socket);
        transport = new TFramedTransport(transport);
        TProtocol protocol = new TBinaryProtocol(transport);
        return new Client(protocol);
    }

    public String signInWithPassword(Client client, String realm,
                                     String username, String password)
        throws TException {
        return client
                .signInWithPassword(UUID.randomUUID().toString(),
                        realm, username, password, new SessionOptions())
                .getAccessToken();
    }


    public List<Token> generateKCTs(Client client, String messageID, String accessToken,
                                    MeterConfigIn meterConfig, MeterConfigAmendment newMeterConfig)
            throws TException {
        return client.issueKeyChangeTokens(messageID, accessToken,
                meterConfig, newMeterConfig);
    }

    public List<Token> generateCreditToken(Client client, String messageID, String accessToken,
                                           MeterConfigIn meterConfig, CreditTokenType resourceType,
                                           float transferAmount, long tokenTime)
            throws TException  {
        if (resourceType.subclass >= 4
                && resourceType.subclass <= 7) {
            transferAmount *= 100000;
        } else {
            transferAmount *= 10;
        }

        int tokenFlag = TokenIssueFlags.EXTERNAL_CLOCK.getValue();
        return client.issueCreditToken(messageID, accessToken, meterConfig,
                resourceType.subclass, transferAmount, tokenTime, tokenFlag);
    }

    public List<Token> generateMseToken(Client client, String messageID, String accessToken,
                                        MseToken mseSubClass, MeterConfigIn meterConfig,
                                        Optional<Float> maxPower, Optional<FlagTokenType> flagTokenType,
                                        Optional<FlagTokenValue> flagTokenValue, long tokenTime)
            throws TException  {
        float transferAmount = 0;
        if (maxPower.isPresent()) {
            transferAmount = maxPower.get();
        } else if (mseSubClass == MseToken.SetFlag) {
            transferAmount = getTokenFlag(flagTokenType.get(), flagTokenValue.get());
        }

        int tokenFlag = TokenIssueFlags.EXTERNAL_CLOCK.getValue();
        return client.issueMseToken(messageID, accessToken, meterConfig,
                mseSubClass.subclass, transferAmount, tokenTime, tokenFlag);
    }


    public MeterTestToken generateNMseToken(Client client, String messageID, String accessToken,
                                            Short subclass, NMseType control, ManufacturerCode manufacturerCode)
            throws TException {
        return client.issueMeterTestToken(messageID, accessToken,
                subclass, control.control, Short.parseShort(manufacturerCode.getValue()));
    }

    public VerifyResult verifyToken(Client client, String messageID, String accessToken,
                            String token, MeterConfigIn meterConfig)
            throws TException {
        return client.verifyToken(messageID, accessToken, meterConfig,
                token);
    }

    private int getTokenFlag(FlagTokenType flagType, FlagTokenValue flagValue) {
        short index = 63;
        String indexBits = convertToBinary(index, 6);
        String flagIndexBits = convertToBinary(flagType.subclass, 9);
        String flagValueBits = convertToBinary(flagValue.subclass, 1);
        return Integer.parseInt(indexBits + flagIndexBits + flagValueBits);
    }

    private String convertToBinary(short index, int length) {
        return String.format("%1$" + length + "s",
                Integer.toBinaryString(index)).replace(' ', '0');
    }
}
