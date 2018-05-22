package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.api.OpenmRDPClientAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPClientApiImpl;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.logger.MrdpDummyLoggerImpl;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messages.OperationType;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) {

        MrdpLogger logger = new MrdpDummyLoggerImpl();
        Parameters params = parseArguments(args, args.length);
        boolean secure = params.getUserName() != null;

        if (!checkParams(params)) {
            System.out.println("Invalid parameters combination");
        }

        String localAddress = null;
        try {
            localAddress = getLocalIpAddress();
        } catch (Exception e) {
            logger.logError(e.getMessage());
        }
        OpenmRDPClientAPI clientAPI = new OpenmRDPClientApiImpl(localAddress + ":27741/", new MrdpDummyLoggerImpl(), true);
        try {
            if (params.getOperationType() == OperationType.LOCATE) {
                String resourceURI;
                if (secure) {
                    resourceURI = clientAPI.locateResource(params.getResource(), params.getUserName(), params.getPassword());
                } else {
                    resourceURI = clientAPI.locateResource(params.getResource());
                }
                printResult(resourceURI);
            } else {
                String query = null;
                if (params.getFileName() != null) {
                    try {
                        query = loadQueryFromFile(params.getFileName());
                    } catch (IOException e) {
                        logger.logError(e.getMessage());
                    }
                } else {
                    query = params.getResource();
                }
                String resourceURI;
                if (secure) {
                    resourceURI = clientAPI.identifyResource(query, params.getUserName(), params.getPassword());
                } else {
                    resourceURI = clientAPI.identifyResource(query);
                }

                printResult(resourceURI);
            }
        } catch (NetworkCommunicationException | QuerySyntaxException e) {

            System.out.println("Servers don't have any information about this resource.");
        }
    }

    private static boolean checkParams(Parameters params) {
        if ((params.getUserName() != null && params.getPassword() == null)
                || (params.getUserName() == null && params.getPassword() != null)) {
            return false;
        }

        if (params.getResource() != null && params.getFileName() != null) {
            return false;
        }

        return true;
    }


    private static void printResult(String result) {
        if (result == null) {
            System.out.println("Servers don't have any information about this resource.");
        } else {
            System.out.println(result);
        }
    }

    private static String loadQueryFromFile(String fileName) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));

        return new String(encoded, "UTF-8");
    }

    private static Parameters parseArguments(String[] args, int argv) {
        Parameters.Builder parametersBuilder = new Parameters.Builder();
        for (int i = 0; i < argv; i = i + 2) {
            String arg = args[i];
            switch (arg) {
                case "-i":
                    parametersBuilder.withOperationType(OperationType.IDENTIFY);
                    parametersBuilder.withResource(args[i + 1]);
                    break;
                case "-if":
                    parametersBuilder.withOperationType(OperationType.IDENTIFY);
                    parametersBuilder.withFileName(args[i + 1]);
                    break;
                case "-l":
                    parametersBuilder.withOperationType(OperationType.LOCATE);
                    parametersBuilder.withResource(args[i + 1]);
                    break;
                case "-e":
                    parametersBuilder.withEndpoint(args[i + 1]);
                    break;
                case "-u":
                    parametersBuilder.withUserName(args[i + 1]);
                    break;
                case "-p":
                    parametersBuilder.withPassword(args[i + 1]);
                    break;
                default:
                    throw new IllegalArgumentException("Wrong input parameters.");
            }
        }

        return parametersBuilder.build();
    }

    private static String getLocalIpAddress() throws Exception {
        String resultIpv6 = "";
        String resultIpv4 = "";

        for (Enumeration en = NetworkInterface.getNetworkInterfaces();
             en.hasMoreElements(); ) {

            NetworkInterface intf = (NetworkInterface) en.nextElement();
            for (Enumeration enumIpAddr = intf.getInetAddresses();
                 enumIpAddr.hasMoreElements(); ) {

                InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    if (inetAddress instanceof Inet4Address) {
                        resultIpv4 = inetAddress.getHostAddress();
                    } else if (inetAddress instanceof Inet6Address) {
                        resultIpv6 = inetAddress.getHostAddress();
                    }
                }
            }
        }
        return ((resultIpv4.length() > 0) ? resultIpv4 : resultIpv6);
    }
}
