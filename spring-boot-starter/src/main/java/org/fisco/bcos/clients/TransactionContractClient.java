package org.fisco.bcos.clients;

import lombok.Data;
import org.fisco.bcos.beans.TransactionInfo;
import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contracts.TransactionContract;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tuples.generated.Tuple10;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TransactionContractClient extends ContractClient {
    TransactionContract contract;

    public TransactionContractClient(Credentials credentials, String contractAddress, Web3j web3j) {
        super(credentials, contractAddress, web3j);
    }

    @Override
    public void load() {
        contract = TransactionContract.load(getContractAddress(), getWeb3j(), getCredentials(), GasConstants.STATIC_GAS_PROVIDER);
    }
    public TransactionInfo getTransactionInfo(String transactionId){
        TransactionInfo ret = new TransactionInfo();
        try {
            Tuple10<BigInteger, String, String, BigInteger, String, String, BigInteger, Boolean, Boolean, Boolean> info = contract.getTransactionInfo(new BigInteger(transactionId)).send();

            DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
            String formatDate = dFormat.format(new Date(info.getValue1().longValue()));
            ret.setTimestamp(formatDate);
            ret.setCopyrightId(info.getValue2());
            ret.setCopyrightTag(info.getValue3());
            ret.setPrice(String.valueOf(info.getValue4()));
            ret.setSellerAddress(info.getValue5());
            ret.setBuyerAddress(info.getValue6());
            ret.setTransactionId(String.valueOf(info.getValue7()));
            ret.setReversing(info.getValue8());
            ret.setReversed(info.getValue9());
            ret.setReverseResult(info.getValue10());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ret;
    }

}
