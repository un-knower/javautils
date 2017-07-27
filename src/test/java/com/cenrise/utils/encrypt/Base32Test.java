package com.cenrise.utils.encrypt;

import junit.framework.TestCase;

public class Base32Test extends TestCase {

    public void testBase32() {
        String text = "There can be miracles when you believe";

        String base32 = Base32.encode(text.getBytes());

        assertEquals("KRUGK4TFEBRWC3RAMJSSA3LJOJQWG3DFOMQHO2DFNYQHS33VEBRGK3DJMV3GK", base32);
        assertEquals(Base32.decode(base32), text);

    }

    public static class CryptoUtil
    {

        private final static byte defaultKey1[] = { ( byte ) 0x93,
                                                   ( byte ) 0xa9,
                                                   ( byte ) 0x0f,
                                                   ( byte ) 0xb4,
                                                   ( byte ) 0x57,
                                                   ( byte ) 0x11,
                                                   ( byte ) 0x8d,
                                                   ( byte ) 0x2c };

        private final static byte defaultKey2[] = { ( byte ) 0x75,
                                                   ( byte ) 0x2c,
                                                   ( byte ) 0xf4,
                                                   ( byte ) 0x5c,
                                                   ( byte ) 0x75,
                                                   ( byte ) 0x15,
                                                   ( byte ) 0xc6,
                                                   ( byte ) 0xa3 };

        /**
         * {@inheritDoc}
         */
        public static String encrypt( String text )
        {
            try
            {
                if ( text.length() == 0 )
                {
                    return text;
                }

                // We encrypt the NUL to simplify decryption
                int length = text.getBytes( "UTF8" ).length + 1;

                // Make the input string length a multiple of DES_BLOCK_BYTES bytes
                if ( ( length % Des.DES_BLOCK_BYTES ) != 0 )
                    length = ( ( length / Des.DES_BLOCK_BYTES ) * Des.DES_BLOCK_BYTES ) + Des.DES_BLOCK_BYTES;
                byte [] source = text.getBytes( "UTF8" );
                byte [] digest = new byte [ length ];

                System.arraycopy(source, 0, digest, 0, source.length);

                // Initialize the encryption keys
                Des des = new Des();
                des.SetKey( true, defaultKey1, defaultKey2 );

                // Encrypt the text
                des.Crypt( digest );

                // Convert the encrypted data to ASCII HEX
                StringBuilder sb = new StringBuilder();
                for ( int i = 0; i < length; i++ )
                {
                    int temp = ( int ) digest[ i ] & 0x000000ff;
                    if ( temp < 16 )
                        sb.append( '0' );
                    sb.append( Integer.toHexString( temp ) );
                }

                return sb.toString();
            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        }

        public static String decrypt( String encrypted )
        {
            try
            {
                if ( encrypted.length() == 0 )
                {
                    return encrypted;
                }

                if ( encrypted.length() % 2 != 0 )
                {
                    throw new IllegalArgumentException( "Cannot decrypt the input length has to be multiple of 2:" + encrypted );
                }

                int len = encrypted.length() / 2;
                byte [] digest = new byte [ len ];

                for ( int i = 0; i < digest.length; i++ )
                {
                    digest[ i ] = ( byte ) Integer.parseInt( encrypted.substring( i * 2, i * 2 + 2 ), 16 );

                }

                // Initialize the encryption keys
                Des des = new Des();
                des.SetKey( false, defaultKey1, defaultKey2 );

                // decrypt
                des.Crypt( digest );

                int finalLen = 0;
                for ( int i = 0; i < digest.length; i++ )
                {
                    if ( digest[ i ] != 0 )
                    {
                        finalLen = i + 1;
                    }
                }

                return new String( digest, 0, finalLen, "UTF8" );

            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }

        }

        /**
         * {@inheritDoc}
         */
        private static String encodePassword(String rawPass)

        {

            // The password may be empty. Not recommended, but.....
            if ( rawPass.length() == 0 )
            {
                return rawPass;
            }

            // We encrypt the NUL to simplify decryption
            int length = rawPass.length() + 1;

            // Make the input string length a multiple of DES_BLOCK_BYTES bytes
            if ( ( length % Des.DES_BLOCK_BYTES ) != 0 )
                length = ( ( length / Des.DES_BLOCK_BYTES ) * Des.DES_BLOCK_BYTES ) + Des.DES_BLOCK_BYTES;

            byte [] source = rawPass.getBytes();
            byte [] digest = new byte [ length ];

            System.arraycopy(source, 0, digest, 0, source.length);
            // Initialize the encryption keys
            Des des = new Des();
            des.SetKey( true, defaultKey1, defaultKey2 );


            // Encrypt the password
            des.Crypt( digest );

            // Convert the encrypted data to ASCII HEX
            StringBuilder sb = new StringBuilder();
            for ( int i = 0; i < length; i++ )
            {
                int temp = ( int ) digest[ i ] & 0x000000ff;
                if ( temp < 16 )
                    sb.append( '0' );
                sb.append( Integer.toHexString( temp ) );
            }

            return sb.toString();
        }

        /**
         * {@inheritDoc}
         */
        public static boolean isPasswordValid( String encPass,
                                        String rawPass,
                                        Object salt )
        {
            String pass1 = "" + encPass;
            String pass2 = encodePassword( rawPass );
            return pass1.equals( pass2 );
        }


        public static void main ( String [] args ) throws Exception
        {

            String db = "63cb6745f7b4324f";
           // String repo="af28dbf3c80e1557";
            System.out.println( CryptoUtil.decrypt( db ) );
           // System.out.println( CryptoUtil.decrypt( repo ));

        }


    }

    /**
     * <p>Title: �ӽ��ܹ��ܽӿ�</p>
     * <p>Description: �ṩ������ܡ��ӽ��ܹ���</p>
     * <p>Copyright: Copyright (c) 2006</p>
     * <p>Company: YeePay</p>
     * @author gaoming.yang
     * @version 2.0
     */
    public static interface CryptUtil {

        /**
         * ����Triple-DES�㷨�������ݽ��м���
         * @param source - ��������
         * @param key - ��Կ
         * @return - ��������
         */
        String cryptDes(String source, String key);

        /**
         * ����Triple-DES�㷨�������ݽ��н���
         * @param des - ��������
         * @param key - ��Կ
         * @return - ��������
         */
        String decryptDes(String des, String key);

        /**
         * ����Triple-DES�㷨��ʹ��ϵͳ�̶�����Կ"a1b2c3d4e5f6g7h8i9j0klmn"�������ݽ��м���
         * @param source - ��������
         * @return - ��������
         */
        String cryptDes(String source);

        /**
         * ����Triple-DES�㷨��ʹ��ϵͳ�̶�����Կ"a1b2c3d4e5f6g7h8i9j0klmn"�������ݽ��н���
         * @param des - ��������
         * @return - ��������
         */
        String decryptDes(String des);

        /**
         * �����ݽ���MD5ǩ��
         * @param source - ��ǩ������
         * @param key - ��Կ
         * @return - ����ǩ�����
         */
        String cryptMd5(String source, String key);
    }
}