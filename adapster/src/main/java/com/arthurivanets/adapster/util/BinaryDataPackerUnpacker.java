package com.arthurivanets.adapster.util;

/**
 * Created by paul
 */
public final class BinaryDataPackerUnpacker {


    private static final int OCTET_1_OFFSET = 0;
    private static final int OCTET_2_OFFSET = 8;
    private static final int OCTET_3_OFFSET = 16;
    private static final int OCTET_4_OFFSET = 24;

    private static final int BASE_MASK = 0b1111_1111;




    public static int one(int octet1) {
        return two(octet1, 0);
    }




    public static int two(int octet1, int octet2) {
        return three(octet1, octet2, 0);
    }




    public static int three(int octet1, int octet2, int octet3) {
        return four(octet1, octet2, octet3, 0);
    }




    public static int four(int octet1, int octet2, int octet3, int octet4) {
        final int adjustedOctet1 = (octet1 & BASE_MASK);
        final int adjustedOctet2 = (octet2 & BASE_MASK);
        final int adjustedOctet3 = (octet3 & BASE_MASK);
        final int adjustedOctet4 = (octet4 & BASE_MASK);

        return ((adjustedOctet4 << OCTET_4_OFFSET)
                | (adjustedOctet3 << OCTET_3_OFFSET)
                | (adjustedOctet2 << OCTET_2_OFFSET)
                | (adjustedOctet1 << OCTET_1_OFFSET));
    }




    public static UnpackedData unpack(int packedData) {
        return new UnpackedData(packedData);
    }




    public static class UnpackedData {

        public final int octet1;
        public final int octet2;
        public final int octet3;
        public final int octet4;


        private UnpackedData(int packedData) {
            this.octet1 = ((packedData >> OCTET_1_OFFSET) & BASE_MASK);
            this.octet2 = ((packedData >> OCTET_2_OFFSET) & BASE_MASK);
            this.octet3 = ((packedData >> OCTET_3_OFFSET) & BASE_MASK);
            this.octet4 = ((packedData >> OCTET_4_OFFSET) & BASE_MASK);
        }


    }




}
