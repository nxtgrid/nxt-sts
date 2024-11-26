package co.nxtgrid.hsm.prism.impl;

import co.nxtgrid.hsm.prism.*;
import co.nxtgrid.hsm.prism.impl.exceptions.InvalidDecoderReferenceNumberException;
import co.nxtgrid.hsm.prism.impl.exceptions.InvalidTokenIdentifierException;
import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.rate.InvalidRateException;
import co.nxtgrid.token.domain.rate.Rate;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.class0.TransferElectricityCreditToken;
import co.nxtgrid.token.domain.token.class0.TransferGasCreditToken;
import co.nxtgrid.token.domain.token.class0.TransferWaterCreditToken;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay1Token;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay2Token;
import co.nxtgrid.token.domain.token.class2.*;
import co.nxtgrid.token.domain.tokenclass.class1.InitiateMeterTestDisplayTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class1.InitiateMeterTestDisplay1TokenSubClass;
import co.nxtgrid.token.domain.tokensubclass.class1.InitiateMeterTestDisplay2TokenSubClass;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.miscellaneous.Strings;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidTokenNoException;
import org.apache.thrift.TException;
import org.joda.time.DateTime;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.time.Instant;

public class PrismClientFacade {

    private String host;
    private int port;
    private String realm;
    private String username;
    private String password;
    private String accessToken;
    private TokenApi.Client client;
    public MeterConfigIn meterConfigIn;
    protected PrismHSMConnector connector;

    public PrismClientFacade(String host, int port, String realm,
                             String username, String password,
                             PrismHSMConnector connector) {
        setHost(host);
        setPort(port);
        setRealm(realm);
        setUsername(username);
        setPassword(password);
        setPrismHSMConnector(connector);
    }

    protected MeterConfigIn generateMeterConfig(IndividualAccountIdentificationNumber iain,
                                       EncryptionAlgorithm encryptionAlgorithm,
                                       TokenCarrierType tokenCarrierType,
                                       SupplyGroupCode supplyGroupCode,
                                       KeyRevisionNumber keyRevisionNumber,
                                       KeyExpiryNumber keyExpiryNumber,
                                       TariffIndex tariffIndex,
                                       boolean allow3Kct) {
        meterConfigIn = new MeterConfigIn();
        meterConfigIn.setDrn(iain.getValue());
        meterConfigIn.setEa(Short.parseShort(encryptionAlgorithm.getCode().getName()));
        meterConfigIn.setTct(Short.parseShort(tokenCarrierType.getValue().getCode()));
        meterConfigIn.setSgc(Integer.parseInt(supplyGroupCode.getValue()));
        meterConfigIn.setKrn((short) keyRevisionNumber.getValue());
        meterConfigIn.setTi(Short.parseShort(tariffIndex.getValue()));
        meterConfigIn.setKen((short) keyExpiryNumber.getValue());
        meterConfigIn.setAllow3Kct(allow3Kct);
        meterConfigIn.setAllowKrnUpdate(false);
        meterConfigIn.setAllowKenUpdate(false);
        return meterConfigIn;
    }

    protected void authenticate()
            throws TException, KeyManagementException,
                IOException, NoSuchAlgorithmException {
        client = connector.getClient(getHost(), getPort());
        accessToken = connector.signInWithPassword(client, getRealm(), getUsername(), getPassword());
    }

    public void initialize(IndividualAccountIdentificationNumber iain,
                              EncryptionAlgorithm encryptionAlgorithm,
                              TokenCarrierType tokenCarrierType,
                              SupplyGroupCode supplyGroupCode,
                              KeyRevisionNumber keyRevisionNumber,
                              KeyExpiryNumber keyExpiryNumber,
                              TariffIndex tariffIndex,
                              boolean allow3Kct)
            throws TException, KeyManagementException,
            IOException, NoSuchAlgorithmException {
        authenticate();
        meterConfigIn = generateMeterConfig(iain, encryptionAlgorithm, tokenCarrierType, supplyGroupCode,
                                            keyRevisionNumber, keyExpiryNumber, tariffIndex, allow3Kct);

    }

    public TransferElectricityCreditToken generateTransferElectricityCreditToken(String requestID,
                                                                                 IndividualAccountIdentificationNumber
                                                                                         individualAccountIdentificationNumber,
                                                                                 EncryptionAlgorithm encryptionAlgorithm,
                                                                                 TokenCarrierType tokenCarrierType,
                                                                                 SupplyGroupCode supplyGroupCode,
                                                                                 KeyRevisionNumber keyRevisionNumber,
                                                                                 Amount amountPurchased,
                                                                                 KeyExpiryNumber keyExpiryNumber,
                                                                                 TariffIndex tariffIndex)
            throws TException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            InvalidRangeException, InvalidBitStringException,
            InvalidUnitsPurchasedException, InvalidTokenIdentifierException {
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                    tokenCarrierType, supplyGroupCode,
                    keyRevisionNumber, keyExpiryNumber, tariffIndex, false);
        long tokenTime = Instant.now().getEpochSecond();
        List<Token> tokens = connector.generateCreditToken(client, requestID, accessToken,
                meterConfigIn, PrismHSMConnector.CreditTokenType.Electricity,
                (float) amountPurchased.getAmountPurchased(), tokenTime);

        TransferElectricityCreditToken creditToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("Credit:Electricity")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                creditToken
                        = new TransferElectricityCreditToken(requestID,
                                                            new TokenIdentifier(tid, baseDate),
                                                            Optional.empty(),
                                                            new Amount(Double.parseDouble(token.getScaledAmount())));
                creditToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return creditToken;
    }

    public TransferWaterCreditToken generateTransferWaterCreditToken(String requestID,
                                                                     IndividualAccountIdentificationNumber
                                                                             individualAccountIdentificationNumber,
                                                                     EncryptionAlgorithm encryptionAlgorithm,
                                                                     TokenCarrierType tokenCarrierType,
                                                                     SupplyGroupCode supplyGroupCode,
                                                                     KeyRevisionNumber keyRevisionNumber,
                                                                     Amount amountPurchased,
                                                                     KeyExpiryNumber keyExpiryNumber,
                                                                     TariffIndex tariffIndex)
            throws TException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            InvalidRangeException, InvalidBitStringException,
            InvalidUnitsPurchasedException, InvalidTokenIdentifierException {
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);
        long tokenTime = Instant.now().getEpochSecond();
        List<Token> tokens = connector.generateCreditToken(client, requestID, accessToken,
                meterConfigIn, PrismHSMConnector.CreditTokenType.Water,
                (float) amountPurchased.getAmountPurchased(), tokenTime);

        TransferWaterCreditToken creditToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("Credit:Water")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                creditToken
                        = new TransferWaterCreditToken(
                                requestID,
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty(),
                                new Amount(Double.parseDouble(token.getScaledAmount())));
                creditToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return creditToken;
    }

    public TransferGasCreditToken generateTransferGasCreditToken(String requestID,
                                                                 IndividualAccountIdentificationNumber
                                                                         individualAccountIdentificationNumber,
                                                                 EncryptionAlgorithm encryptionAlgorithm,
                                                                 TokenCarrierType tokenCarrierType,
                                                                 SupplyGroupCode supplyGroupCode,
                                                                 KeyRevisionNumber keyRevisionNumber,
                                                                 Amount amountPurchased,
                                                                 KeyExpiryNumber keyExpiryNumber,
                                                                 TariffIndex tariffIndex)
            throws TException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            InvalidRangeException, InvalidBitStringException,
            InvalidUnitsPurchasedException, InvalidTokenIdentifierException {
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);
        long tokenTime = Instant.now().getEpochSecond();
        List<Token> tokens = connector.generateCreditToken(client, requestID, accessToken,
                meterConfigIn, PrismHSMConnector.CreditTokenType.Gas,
                (float) amountPurchased.getAmountPurchased(), tokenTime);

        TransferGasCreditToken creditToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("Credit:Gas")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                creditToken
                        = new TransferGasCreditToken(
                                requestID,
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty(),
                                new Amount(Double.parseDouble(token.getScaledAmount())));
                creditToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return creditToken;
    }

    public InitiateMeterTestOrDisplay1Token generateInitiateMeterTestOrDisplay1Token(
            String requestID, IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
            Control control, ManufacturerCode manufacturerCode)
                throws TException, InvalidDecoderReferenceNumberException,
                    IOException, NoSuchAlgorithmException, KeyManagementException,
                    InvalidControlException, InvalidRangeException {
        final short SUBCLASS = 0;
        authenticate();
        if (individualAccountIdentificationNumber.getValue().length() != 11) {
            throw new InvalidDecoderReferenceNumberException(String.format(
                    "%s is not valid for this token type",
                    individualAccountIdentificationNumber.getValue()));
        }
        MeterTestToken genToken = connector.generateNMseToken(client, requestID, accessToken,
                                                                SUBCLASS,
                                                                getNMSEType(control), manufacturerCode);
        InitiateMeterTestOrDisplay1Token token
                = new InitiateMeterTestOrDisplay1Token(requestID, new InitiateMeterTestDisplayTokenClass(),
                                                        new InitiateMeterTestDisplay1TokenSubClass(),
                                                        control, manufacturerCode);
        token.setEncryptedTokenBitString(new BigInteger(genToken.getTokenDec()).toString(2));
        return token;
    }

    public InitiateMeterTestOrDisplay2Token generateInitiateMeterTestOrDisplay2Token(
            String requestID, IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
            Control control, ManufacturerCode manufacturerCode)
                throws TException, InvalidDecoderReferenceNumberException,
                    IOException, NoSuchAlgorithmException, KeyManagementException,
                    InvalidControlException, InvalidRangeException {
        authenticate();
        final short SUBCLASS = 1;
        if (individualAccountIdentificationNumber.getValue().length() != 13) {
            throw new
                    InvalidDecoderReferenceNumberException(String.format(
                            "%s is not valid for this token type",
                    individualAccountIdentificationNumber.getValue()));
        }
        MeterTestToken genToken = connector.generateNMseToken(client, requestID, accessToken,
                                                                SUBCLASS, getNMSEType(control), manufacturerCode);
        InitiateMeterTestOrDisplay2Token token
                = new InitiateMeterTestOrDisplay2Token(requestID, new InitiateMeterTestDisplayTokenClass(),
                                                        new InitiateMeterTestDisplay2TokenSubClass(),
                                                        control, manufacturerCode);
        token.setEncryptedTokenBitString(new BigInteger(genToken.getTokenDec()).toString(2));
        return token;
    }

    public ClearCreditToken generateClearCreditToken(String requestID,
                                                     IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                     EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                     SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                     KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.ClearCredit, meterConfigIn, Optional.empty(),
                Optional.empty(), Optional.empty(), tokenTime);

        ClearCreditToken clearCreditToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("ClearCredit")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                clearCreditToken = new ClearCreditToken(requestID,
                                                        Optional.empty(),
                                                        new TokenIdentifier(tid, baseDate),
                                                        Optional.empty());
                clearCreditToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return clearCreditToken;
    }

    public ClearTamperConditionToken generateClearTamperConditionToken(String requestID,
                                                                       IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                                       EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                                       SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                                       KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.ClearTamper, meterConfigIn, Optional.empty(),
                Optional.empty(), Optional.empty(), tokenTime);

        ClearTamperConditionToken clearTamperConditionToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("ClearTamperCondition")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                clearTamperConditionToken
                        = new ClearTamperConditionToken(
                                requestID,
                                Optional.empty(),
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty());
                clearTamperConditionToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return clearTamperConditionToken;
    }

    public List<Class2Token> generateDecoderKeyTokens(String requestID,
                                                      IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                      EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                      SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                      KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex,
                                                      SupplyGroupCode toSgc, KeyRevisionNumber toKrn,
                                                      TariffIndex toTi, boolean allow3Kct)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidRangeException {

        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, allow3Kct);

        List<Token> tokens = connector.generateKCTs(client, requestID, accessToken,
                meterConfigIn, new MeterConfigAmendment(Integer.parseInt(toSgc.getValue()),
                        (short) toKrn.getValue(), Short.parseShort(toTi.getValue())));

        List<Class2Token> tokenList = new ArrayList<>();

        for (Token token : tokens) {
            if (token.getDescription().equals("Set1stSectionDecoderKey")) {
                Set1stSectionDecoderKeyToken set1stSectionDecoderKeyToken
                        = new Set1stSectionDecoderKeyToken(requestID);
                set1stSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                tokenList.add(set1stSectionDecoderKeyToken);
            } else if (token.getDescription().equals("Set2ndSectionDecoderKey")) {
                Set2ndSectionDecoderKeyToken set2ndSectionDecoderKeyToken
                        = new Set2ndSectionDecoderKeyToken(requestID);
                set2ndSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                tokenList.add(set2ndSectionDecoderKeyToken);
            } else if (token.getDescription().equals("Set3rdSectionDecoderKey")) {
                Set3rdSectionDecoderKeyToken set3rdSectionDecoderKeyToken
                        = new Set3rdSectionDecoderKeyToken(requestID);
                set3rdSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                tokenList.add(set3rdSectionDecoderKeyToken);
            }
        }
        return tokenList;
    }

    public SetMaximumPhasePowerUnbalanceLimitToken generateSetMaximumPhasePowerUnbalanceLimitToken(
                String requestID,
                IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.SetMaximumPhasePowerUnbalanceLimit, meterConfigIn, Optional.empty(),
                Optional.empty(), Optional.empty(), tokenTime);

        SetMaximumPhasePowerUnbalanceLimitToken setMaximumPhasePowerUnbalanceLimitToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("SetMaxPhaseUnbalanceLmt")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                setMaximumPhasePowerUnbalanceLimitToken
                        = new SetMaximumPhasePowerUnbalanceLimitToken(
                                requestID,
                                Optional.empty(),
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty());
                setMaximumPhasePowerUnbalanceLimitToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return setMaximumPhasePowerUnbalanceLimitToken;
    }

    public SetMaximumPowerLimitToken generateSetMaximumPowerLimitToken(String requestID,
                                                                       MaximumPowerLimit maximumPowerLimit,
                                                                       PrismHSMConnector.FlagTokenType flagTokenType,
                                                                       PrismHSMConnector.FlagTokenValue flagTokenValue,
                                                                       IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                                       EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                                       SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                                       KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.SetMaximumPowerLimit, meterConfigIn,
                Optional.of((float) maximumPowerLimit.getMaximumPowerLimit()),
                Optional.of(flagTokenType), Optional.of(flagTokenValue), tokenTime);

        SetMaximumPowerLimitToken setMaximumPhasePowerUnbalanceLimitToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("SetMaximumPowerLimit")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                setMaximumPhasePowerUnbalanceLimitToken
                        = new SetMaximumPowerLimitToken(
                                requestID,
                                Optional.empty(),
                                new TokenIdentifier(tid, baseDate),
                                maximumPowerLimit,
                                Optional.empty());
                setMaximumPhasePowerUnbalanceLimitToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return setMaximumPhasePowerUnbalanceLimitToken;
    }

    public SetTariffRateToken generateSetTariffRateToken(String requestID,
                                                         IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                         EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                         SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                         KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.SetTariffRate, meterConfigIn, Optional.empty(),
                Optional.empty(), Optional.empty(), tokenTime);

        SetTariffRateToken setTariffRateToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("SetTariffRate")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                setTariffRateToken
                        = new SetTariffRateToken(
                                requestID,
                                Optional.empty(),
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty());
                setTariffRateToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return setTariffRateToken;
    }

    public SetWaterMeterFactorToken generateSetWaterMeterFactorToken(String requestID,
                                                                     IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                                                     EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                                                                     SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                                                                     KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex)
            throws TException, KeyManagementException,
            NoSuchAlgorithmException, IOException, InvalidTokenIdentifierException,
            InvalidRangeException {
        long tokenTime = Instant.now().getEpochSecond();
        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        List<Token> tokens = connector.generateMseToken(client, requestID, accessToken,
                PrismHSMConnector.MseToken.SetWaterMeterFactor, meterConfigIn, Optional.empty(),
                Optional.empty(), Optional.empty(), tokenTime);

        SetWaterMeterFactorToken setWaterMeterFactorToken = null;

        for (Token token : tokens) {
            if (token.getDescription().equals("SetWaterMeterFactor")) {
                BaseDate baseDate = getBaseDate(token.getTid());
                DateTime tid = getDateTime(token.getTid(), baseDate);
                setWaterMeterFactorToken
                        = new SetWaterMeterFactorToken(
                                requestID,
                                Optional.empty(),
                                new TokenIdentifier(tid, baseDate),
                                Optional.empty());
                setWaterMeterFactorToken.setEncryptedTokenBitString(new BigInteger(token.getTokenDec()).toString(2));
                break;
            }
        }
        return setWaterMeterFactorToken;
    }

    public co.nxtgrid.token.domain.token.Token verifyToken(String requestID,
                            IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                            EncryptionAlgorithm encryptionAlgorithm, TokenCarrierType tokenCarrierType,
                            SupplyGroupCode supplyGroupCode, KeyRevisionNumber keyRevisionNumber,
                            KeyExpiryNumber keyExpiryNumber, TariffIndex tariffIndex,
                            String token)
            throws  TException, IOException, KeyManagementException,
                    NoSuchAlgorithmException, InvalidTokenNoException,
                    InvalidRangeException, InvalidTokenSubclassException,
                    InvalidTokenClassException, InvalidTokenIdentifierException,
                    InvalidBitStringException, InvalidUnitsPurchasedException,
                    InvalidManufacturerCodeException, InvalidControlBitStringException,
                    InvalidMPLException, InvalidRegisterBitString, InvalidRateException,
            InvalidMppulException {

        initialize(individualAccountIdentificationNumber, encryptionAlgorithm,
                tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex, false);

        VerifyResult result = connector.verifyToken(client, requestID, accessToken,
                                                    token, meterConfigIn);

        if (result.validationResult.equals("EVerify.Ok")) {

            short tokenClass = result.token != null ? result.token.tokenClass : result.meterTestToken.tokenClass;
            short tokenSubclass = result.token != null ? result.token.subclass : result.meterTestToken.subclass;

            BaseDate baseDate = null;
            DateTime tid = null;

            if (tokenClass >= 0 && tokenClass <= 2) {
                switch (tokenClass) {
                    case (0):
                        baseDate = getBaseDate(result.token.getTid());
                        tid = getDateTime(result.token.getTid(), baseDate);
                        if (tokenSubclass == 0) {
                            TransferElectricityCreditToken transferElectricityCreditToken =
                                    new TransferElectricityCreditToken(requestID,
                                                                        new TokenIdentifier(tid, baseDate),
                                                                        Optional.empty(),
                                                                        new Amount(Double.parseDouble(result.token.scaledAmount)));
                            transferElectricityCreditToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return transferElectricityCreditToken;

                        } else if (tokenSubclass == 1) {
                            TransferWaterCreditToken transferWaterCreditToken =
                                    new TransferWaterCreditToken(requestID,
                                                                new TokenIdentifier(tid, baseDate),
                                                                Optional.empty(),
                                                                new Amount(Double.parseDouble(result.token.scaledAmount)));
                            transferWaterCreditToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return transferWaterCreditToken;

                        } else if (tokenSubclass == 2) {
                            TransferGasCreditToken transferGasCreditToken =
                                    new TransferGasCreditToken(requestID,
                                                                new TokenIdentifier(tid, baseDate),
                                                                Optional.empty(),
                                                                new Amount(Double.parseDouble(result.token.scaledAmount)));
                            transferGasCreditToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return transferGasCreditToken;

                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    case (1):
                        if (tokenSubclass == 0) {
                            ManufacturerCode manufacturerCode = new ManufacturerCode(String.format("%02d",
                                    result.meterTestToken.mfrcode));
                            BitString controlBitString = new BitString(Long.toBinaryString(result.meterTestToken.control));
                            controlBitString.setLength(36);
                            Control control = new Control(controlBitString, manufacturerCode);
                            InitiateMeterTestOrDisplay1Token initiateMeterTestOrDisplay1Token =
                                    new InitiateMeterTestOrDisplay1Token(requestID,
                                                                        new InitiateMeterTestDisplayTokenClass(),
                                                                        new InitiateMeterTestDisplay1TokenSubClass(),
                                                                        control,
                                                                        manufacturerCode);
                            initiateMeterTestOrDisplay1Token.setEncryptedTokenBitString(new BigInteger(result.meterTestToken.getTokenDec()).toString(2));
                            return initiateMeterTestOrDisplay1Token;

                        } else if (tokenSubclass == 1) {
                            ManufacturerCode manufacturerCode = new ManufacturerCode(String.format("%04d",
                                    result.meterTestToken.mfrcode));
                            BitString controlBitString = new BitString(Long.toBinaryString(result.meterTestToken.control));
                            controlBitString.setLength(28);
                            Control control = new Control(controlBitString, manufacturerCode);
                            InitiateMeterTestOrDisplay2Token initiateMeterTestOrDisplay2Token =
                                    new InitiateMeterTestOrDisplay2Token(requestID,
                                                                        new InitiateMeterTestDisplayTokenClass(),
                                                                        new InitiateMeterTestDisplay2TokenSubClass(),
                                                                        control,
                                                                        manufacturerCode);
                            initiateMeterTestOrDisplay2Token.setEncryptedTokenBitString(new BigInteger(result.meterTestToken.getTokenDec()).toString(2));
                            return initiateMeterTestOrDisplay2Token;

                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    case(2):
                        if (tokenSubclass != 3 && tokenSubclass != 4 &&
                                tokenSubclass != 8 && tokenSubclass != 9) {
                            baseDate = getBaseDate(result.token.getTid());
                            tid = getDateTime(result.token.getTid(), baseDate);
                        }

                        if (tokenSubclass == 0) {
                            SetMaximumPowerLimitToken setMaximumPowerLimitToken =
                                    new SetMaximumPowerLimitToken(requestID,
                                                                Optional.empty(),
                                                                new TokenIdentifier(tid, baseDate),
                                                                new MaximumPowerLimit(Long.parseLong(result.token.scaledAmount)),
                                                                Optional.empty());
                            setMaximumPowerLimitToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return setMaximumPowerLimitToken;

                        } else if (tokenSubclass == 1) {
                            BitString registerBitString = new BitString(result.token.scaledAmount);
                            registerBitString.setLength(16);
                            ClearCreditToken clearCreditToken =
                                    new ClearCreditToken(requestID,
                                                        Optional.empty(),
                                                        new TokenIdentifier(tid, baseDate),
                                                        Optional.of(new Register(registerBitString)));
                            clearCreditToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return clearCreditToken;

                        } else if (tokenSubclass == 2) {
                            BitString rateBitString = new BitString(result.token.scaledAmount);
                            rateBitString.setLength(16);
                            SetTariffRateToken setTariffRateToken =
                                    new SetTariffRateToken(requestID,
                                                            Optional.empty(),
                                                            new TokenIdentifier(tid, baseDate),
                                                            Optional.of(new Rate(rateBitString)));
                            setTariffRateToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return setTariffRateToken;

                        } else if (tokenSubclass == 3) {
                            Set1stSectionDecoderKeyToken set1stSectionDecoderKeyToken =
                                    new Set1stSectionDecoderKeyToken(requestID);
                            set1stSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return set1stSectionDecoderKeyToken;

                        } else if (tokenSubclass == 4) {
                            Set2ndSectionDecoderKeyToken set2ndSectionDecoderKeyToken =
                                    new Set2ndSectionDecoderKeyToken(requestID);
                            set2ndSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return set2ndSectionDecoderKeyToken;

                        } else if (tokenSubclass == 8) {
                            Set3rdSectionDecoderKeyToken set3rdSectionDecoderKeyToken =
                                    new Set3rdSectionDecoderKeyToken(requestID);
                            set3rdSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return set3rdSectionDecoderKeyToken;

                        } else if (tokenSubclass == 9) {
                            Set4thSectionDecoderKeyToken set4thSectionDecoderKeyToken =
                                    new Set4thSectionDecoderKeyToken(requestID);
                            set4thSectionDecoderKeyToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return set4thSectionDecoderKeyToken;

                        } else if (tokenSubclass == 5) {
                            ClearTamperConditionToken clearTamperConditionToken =
                                    new ClearTamperConditionToken(requestID,
                                                                Optional.empty(),
                                                                new TokenIdentifier(tid, baseDate),
                                                                Optional.empty());
                            clearTamperConditionToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return clearTamperConditionToken;

                        } else if (tokenSubclass == 6) {
                            SetMaximumPhasePowerUnbalanceLimitToken setMaximumPhasePowerUnbalanceLimitToken =
                                    new SetMaximumPhasePowerUnbalanceLimitToken(requestID,
                                                                Optional.empty(),
                                                                new TokenIdentifier(tid, baseDate),
                                                                Optional.of(new MaximumPhasePowerUnbalanceLimit(Long.parseLong(result.token.scaledAmount))));
                            setMaximumPhasePowerUnbalanceLimitToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return setMaximumPhasePowerUnbalanceLimitToken;

                        } else if (tokenSubclass == 7) {
                            SetWaterMeterFactorToken setWaterMeterFactorToken =
                                    new SetWaterMeterFactorToken(requestID,
                                                                Optional.empty(),
                                                                new TokenIdentifier(tid, baseDate),
                                                                Optional.empty());
                            setWaterMeterFactorToken.setEncryptedTokenBitString(new BigInteger(result.token.getTokenDec()).toString(2));
                            return setWaterMeterFactorToken;

                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    default:
                        throw new InvalidTokenClassException(Strings.INVALID_TOKEN_CLASS);
                }

            } else {
                throw new InvalidTokenClassException(Strings.INVALID_TOKEN_CLASS);
            }
        }
        throw new InvalidTokenNoException(String.format("%s is not verified", token));
    }

    private PrismHSMConnector.NMseType getNMSEType(Control control)
            throws InvalidControlException {
        long controlVal = control.getBitString().getValue();
        PrismHSMConnector.NMseType nMseType
                = PrismHSMConnector.NMseType.getControl(controlVal);
        if (nMseType != null) {
            return nMseType;
        }
        throw new InvalidControlException(String.format("%s control is unsupported", controlVal));
    }

    private DateTime getDateTime(int tid, BaseDate baseDate) {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.set(Calendar.DATE, baseDate.dateTime.getDayOfMonth());
        date.set(Calendar.MONTH, baseDate.dateTime.getMonthOfYear() - 1); // cater for Joda DateTime
        date.set(Calendar.YEAR, baseDate.dateTime.getYear());

        date.set(Calendar.HOUR, baseDate.dateTime.getHourOfDay());
        date.set(Calendar.MINUTE, baseDate.dateTime.getMinuteOfDay());
        date.set(Calendar.SECOND, baseDate.dateTime.getSecondOfDay());

        date.add(Calendar.MINUTE, tid);
        return new DateTime(date.getTime());
    }

    private BaseDate getBaseDate(int epochTime)
            throws InvalidTokenIdentifierException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -(epochTime));
        DateTime tid = new DateTime(cal.getTime());
        if (tid.getYear() == 1993)
            return BaseDate._1993;
        if (tid.getYear() == 2014)
            return BaseDate._2014;
        if (tid.getYear() == 2035)
            return BaseDate._2035;
        throw new InvalidTokenIdentifierException(
                String.format("%s is an invalid TID", tid.toString()));
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PrismHSMConnector getPrismHSMConnector(PrismHSMConnector connector) {
        return connector;
    }

    public void setPrismHSMConnector(PrismHSMConnector connector) {
        this.connector = connector;
    }
}
