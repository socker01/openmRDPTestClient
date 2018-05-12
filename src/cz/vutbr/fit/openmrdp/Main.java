package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.api.OpenmRDPClientAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPClientApiImpl;
import cz.vutbr.fit.openmrdp.logger.MrdpTestLoggerImpl;
import cz.vutbr.fit.openmrdp.messages.OperationType;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws Exception {
        Parameters params = parseArguments(args, args.length);

        OpenmRDPClientAPI clientAPI = new OpenmRDPClientApiImpl(getLocalIpAddress() + ":27741/", new MrdpTestLoggerImpl());
        if (params.getOperationType() == OperationType.LOCATE) {
            String resourceURI = clientAPI.locateResource(params.getResource());
            System.out.println(resourceURI);
        } else {
            String resourceURI = clientAPI.identifyResource(params.getResource());
            System.out.println(resourceURI);
        }
    }

    private static Parameters parseArguments(String[] args, int argv) {
        OperationType operationType = null;
        String resource = null;
        String endpoint = null;
        for (int i = 0; i < argv; i = i + 2) {
            String arg = args[i];
            switch (arg) {
                case "-i":
                case "-if":
                    operationType = OperationType.IDENTIFY;
                    resource = args[i + 1];
                    break;
                case "-l":
                    operationType = OperationType.LOCATE;
                    resource = args[i + 1];
                    break;
                case "-e":
                    endpoint = args[i + 1];
                    break;
                default:
                    throw new IllegalArgumentException("Wrong input parameters.");

            }
        }

        return new Parameters(operationType, resource, endpoint);
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
