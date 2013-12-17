package ph.romo;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Main {
	static SerialPort serialPort;
	
    public static void main(String[] args) {
        serialPort = new SerialPort("COM3");
        
        try {
            serialPort.openPort();
            serialPort.setParams(9600, 8, 1, 0);
            
            String message = "02003536303030303030303030313032303030301C343000123030303030303030313030301C0314";
            
            System.out.println("[WRITING MESSAGE]");
            serialPort.writeBytes(hexStringToByteArray(message));
            
//            System.out.println("[RECEIVING RESPONSE]");
//            String buffer = serialPort.readString(1024);
            
            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
            
            System.out.println("[ADDING LISTENER]");
            serialPort.addEventListener(new SerialPortReader());
            
//            System.out.println("[CLOSING PORT]");
//            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }	

    static class SerialPortReader implements SerialPortEventListener {

    	@Override
    	public void serialEvent(SerialPortEvent event) {
			if(event.isRXCHAR()){
				try {
					boolean ack = serialPort.writeBytes(hexStringToByteArray("06"));
					
					if (ack) {
						System.out.println("[SENDING ACKNOWLEDGE]");
					}
					
				} catch (SerialPortException e) {
					e.printStackTrace();
				} 
			}
        }
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}