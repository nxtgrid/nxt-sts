package co.nxtgrid.token.domain.encryptionalgorithm;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;

public class Misty1AlgorithmEncryptionAlgorithm extends EncryptionAlgorithm {

    public Misty1AlgorithmEncryptionAlgorithm(){
        super(Code.MISTY1);
    }

    public BitString encrypt(DecoderKey decoderKey, BitString dataBlock)
            throws Exception {
        Misty1Functions hxdx = new Misty1Functions();
        char[] newPlainText = dataBlock.toCharArr();
        char[] newKey = decoderKey.toCharArr();
        char cipherText[] = new char[8];
        long[] ek = hxdx.KeySchedule(newKey);
        hxdx.encrypt(newPlainText, ek, cipherText);
        return new BitString(charArrToStr(cipherText));
    }

    public BitString decrypt(DecoderKey decoderKey, BitString cipherBlock)
            throws Exception {
        Misty1Functions hxdx = new Misty1Functions();
        char[] newCipherText = cipherBlock.toCharArr();
        char newKey[] = decoderKey.toCharArr();
        char plainText[] = new char[8];
        long[] ek = hxdx.KeySchedule(newKey);
        hxdx.decrypt(newCipherText, ek, plainText);
        return new BitString(charArrToStr(plainText));
    }

    private String charArrToStr(char[] arr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            String bufferedRes = "000000000000000" + Integer.toBinaryString(arr[i]);
            stringBuffer.append(bufferedRes.substring(bufferedRes.length() - 8));
        }
        return stringBuffer.toString();
    }
}

class Misty1Functions
{
    int s7[] = { 27, 50, 51, 90, 59, 16, 23, 84, 91, 26, 114, 115, 107, 44,
            102, 73, 31, 36, 19, 108, 55, 46, 63, 74, 93, 15, 64, 86, 37, 81,
            28, 4, 11, 70, 32, 13, 123, 53, 68, 66, 43, 30, 65, 20, 75, 121,
            21, 111, 14, 85, 9, 54, 116, 12, 103, 83, 40, 10, 126, 56, 2, 7,
            96, 41, 25, 18, 101, 47, 48, 57, 8, 104, 95, 120, 42, 76, 100, 69,
            117, 61, 89, 72, 3, 87, 124, 79, 98, 60, 29, 33, 94, 39, 106, 112,
            77, 58, 1, 109, 110, 99, 24, 119, 35, 5, 38, 118, 0, 49, 45, 122,
            127, 97, 80, 34, 17, 6, 71, 22, 82, 78, 113, 62, 105, 67, 52, 92,
            88, 125 };
    int s9[] = { 451, 203, 339, 415, 483, 233, 251, 53, 385, 185, 279, 491,
            307, 9, 45, 211, 199, 330, 55, 126, 235, 356, 403, 472, 163, 286,
            85, 44, 29, 418, 355, 280, 331, 338, 466, 15, 43, 48, 314, 229,
            273, 312, 398, 99, 227, 200, 500, 27, 1, 157, 248, 416, 365, 499,
            28, 326, 125, 209, 130, 490, 387, 301, 244, 414, 467, 221, 482,
            296, 480, 236, 89, 145, 17, 303, 38, 220, 176, 396, 271, 503, 231,
            364, 182, 249, 216, 337, 257, 332, 259, 184, 340, 299, 430, 23,
            113, 12, 71, 88, 127, 420, 308, 297, 132, 349, 413, 434, 419, 72,
            124, 81, 458, 35, 317, 423, 357, 59, 66, 218, 402, 206, 193, 107,
            159, 497, 300, 388, 250, 406, 481, 361, 381, 49, 384, 266, 148,
            474, 390, 318, 284, 96, 373, 463, 103, 281, 101, 104, 153, 336, 8,
            7, 380, 183, 36, 25, 222, 295, 219, 228, 425, 82, 265, 144, 412,
            449, 40, 435, 309, 362, 374, 223, 485, 392, 197, 366, 478, 433,
            195, 479, 54, 238, 494, 240, 147, 73, 154, 438, 105, 129, 293, 11,
            94, 180, 329, 455, 372, 62, 315, 439, 142, 454, 174, 16, 149, 495,
            78, 242, 509, 133, 253, 246, 160, 367, 131, 138, 342, 155, 316,
            263, 359, 152, 464, 489, 3, 510, 189, 290, 137, 210, 399, 18, 51,
            106, 322, 237, 368, 283, 226, 335, 344, 305, 327, 93, 275, 461,
            121, 353, 421, 377, 158, 436, 204, 34, 306, 26, 232, 4, 391, 493,
            407, 57, 447, 471, 39, 395, 198, 156, 208, 334, 108, 52, 498, 110,
            202, 37, 186, 401, 254, 19, 262, 47, 429, 370, 475, 192, 267, 470,
            245, 492, 269, 118, 276, 427, 117, 268, 484, 345, 84, 287, 75, 196,
            446, 247, 41, 164, 14, 496, 119, 77, 378, 134, 139, 179, 369, 191,
            270, 260, 151, 347, 352, 360, 215, 187, 102, 462, 252, 146, 453,
            111, 22, 74, 161, 313, 175, 241, 400, 10, 426, 323, 379, 86, 397,
            358, 212, 507, 333, 404, 410, 135, 504, 291, 167, 440, 321, 60,
            505, 320, 42, 341, 282, 417, 408, 213, 294, 431, 97, 302, 343, 476,
            114, 394, 170, 150, 277, 239, 69, 123, 141, 325, 83, 95, 376, 178,
            46, 32, 469, 63, 457, 487, 428, 68, 56, 20, 177, 363, 171, 181, 90,
            386, 456, 468, 24, 375, 100, 207, 109, 256, 409, 304, 346, 5, 288,
            443, 445, 224, 79, 214, 319, 452, 298, 21, 6, 255, 411, 166, 67,
            136, 80, 351, 488, 289, 115, 382, 188, 194, 201, 371, 393, 501,
            116, 460, 486, 424, 405, 31, 65, 13, 442, 50, 61, 465, 128, 168,
            87, 441, 354, 328, 217, 261, 98, 122, 33, 511, 274, 264, 448, 169,
            285, 432, 422, 205, 243, 92, 258, 91, 473, 324, 502, 173, 165, 58,
            459, 310, 383, 70, 225, 30, 477, 230, 311, 506, 389, 140, 143, 64,
            437, 190, 120, 0, 172, 272, 350, 292, 2, 444, 162, 234, 112, 508,
            278, 348, 76, 450 };


    public long[] KeySchedule(char key[]) {
        long ek[] = new long[32];

        // This process divides the 128 bit key into 8 16 bit parts
        // to generate the first 8 sub-keys
        for (int i = 0; i < 8; i++)
            ek[i] = (((int) key[i * 2]) << 8) + ((int) key[i * 2 + 1]);

        // This process generates the second group of sub-keys
        // by putting the first group through the
        // basic operation used within the MISTY F function (fi)
        for (int i = 0; i < 8; i++) {
            ek[i + 8] = fi(ek[i], ek[(i + 1) % 8]);
            ek[i + 16] = ek[i + 8] & 0x1ff;
            ek[i + 24] = ek[i + 8] >> 9;
        }

        return ek;
    }

    public long fl(long[] ek, long fl_in, int k)
    {
        long dl, dr;
        dl = fl_in >> 16;
        dr = fl_in & 0x0000FFFF;
        int t;

        if (k % 2 != 0) {
            t = (k - 1) / 2;
            dr = dr ^ (dl & ek[((t + 2) % 8) + 8]);
            dl = dl ^ (dr | ek[(t + 4) % 8]);
        } else {
            t = k / 2;
            dr = dr ^ (dl & ek[t]);
            dl = dl ^ (dr | ek[((t + 6) % 8) + 8]);
        }

        return (dl << 16) | dr;
    }

    public long fo(long[] ek, long fo_in, int k)
    {
        long t0, t1;
        t0 = (fo_in >> 16);
        t1 = fo_in & 0xFFFF;
        t0 ^= ek[k];
        t0 = fi(t0, ek[((k + 5) % 8) + 8]);
        t0 ^= t1;
        t1 ^= ek[(k + 2) % 8];
        t1 = fi(t1, ek[((k + 1) % 8) + 8]);
        t1 ^= t0;
        t0 ^= ek[(k + 7) % 8];
        t0 = fi(t0, ek[((k + 3) % 8) + 8]);
        t0 ^= t1;
        t1 ^= ek[(k + 4) % 8];
        return ((t1 << 16) | t0);
    }

    public long fi(long fi_in, long fi_key)
    {
        long d9, d7;

        d9 = (fi_in >> 7) & 0x1ff;
        d7 = fi_in & 0x7f;
        d9 = s9[(int) d9] ^ d7;
        d7 = (s7[(int) d7] ^ d9) & 0x7f;

        d7 = d7 ^ ((fi_key >> 9) & 0x7f);
        d9 = d9 ^ (fi_key & 0x1ff);
        d9 = s9[(int) d9] ^ d7;
        return ((d7 << 9) | d9);
    }

    public long flinv(long[] ek, long fl_in, int k)
    {
        long d0, d1;
        int t;

        d0 = (fl_in >> 16);
        d1 = fl_in & 0xffff;

        if (k % 2 != 0) {
            t = (k - 1) / 2;
            d0 = d0 ^ (d1 | ek[(t + 4) % 8]);
            d1 = d1 ^ (d0 & ek[((t + 2) % 8) + 8]);
        } else {
            t = k / 2;
            d0 = d0 ^ (d1 | ek[((t + 6) % 8) + 8]);
            d1 = d1 ^ (d0 & ek[t]);
        }
        return ((d0 << 16) | d1);
    }

    public void encrypt(char ptext[], long[] ek, char ctext[]) {

        long L0, L1, L2, L3, L4, L5, L6, L7, L8, L9, R0, R1, R2, R3, R4, R5, R6, R7, R8, R9;

        L0 = (((long) ptext[0]) << 24) + (((long) ptext[1]) << 16)
                + (((long) ptext[2]) << 8) + ((long) ptext[3]);
        R0 = (((long) ptext[4]) << 24) + (((long) ptext[5]) << 16)
                + (((long) ptext[6]) << 8) + ((long) ptext[7]);

        /* R0 */
        R1 = fl(ek, L0, 0);
        L1 = fo(ek, R1, 0) ^ fl(ek, R0, 1);

        /* R1 */
        R2 = L1;
        L2 = fo(ek, R2, 1) ^ R1;

        /* R2 */
        R3 = fl(ek, L2, 2);
        L3 = fo(ek, R3, 2) ^ fl(ek, R2, 3);

        /* R3 */
        R4 = L3;
        L4 = fo(ek, R4, 3) ^ R3;

        /* R4 */
        R5 = fl(ek, L4,4);
        L5 = fo(ek, R5, 4) ^ fl(ek, R4, 5);

        /* R5 */
        R6 = L5;
        L6 = fo(ek, R6, 5) ^ R5;

        /* R6 */
        R7 = fl(ek, L6, 6);
        L7 = fo(ek, R7, 6) ^ fl(ek, R6, 7);

        /* R7 */
        R8 = L7;
        L8 = fo(ek, R8, 7) ^ R7;

        /* R8 */
        R9 = fl(ek, L8, 8);
        L9 = fl(ek, R8, 9);

        ctext[0] = (char) ((L9 >> 24) & 0XFF);
        ctext[1] = (char) ((L9 >> 16) & 0XFF);
        ctext[2] = (char) ((L9 >> 8) & 0XFF);
        ctext[3] = (char) (L9 & 0XFF);
        ctext[4] = (char) ((R9 >> 24) & 0XFF);
        ctext[5] = (char) ((R9 >> 16) & 0XFF);
        ctext[6] = (char) ((R9 >> 8) & 0XFF);
        ctext[7] = (char) (R9 & 0XFF);

    }

    public void decrypt(char ctext[], long[] ek, char ptext[]) {

        long L0, L1, L2, L3, L4, L5, L6, L7, L8, L9, R0, R1, R2, R3, R4, R5, R6, R7, R8, R9;

        L9 = (((long) ctext[0]) << 24) + (((long) ctext[1]) << 16)
                + (((long) ctext[2]) << 8) + ((long) ctext[3]);
        R9 = (((long) ctext[4]) << 24) + (((long) ctext[5]) << 16)
                + (((long) ctext[6]) << 8) + ((long) ctext[7]);

        /* R8 */
        R8 = flinv(ek, L9, 9);
        L8 = fo(ek, R8, 7) ^ flinv(ek, R9, 8);

        /* R7 */
        R7 = L8;
        L7 = fo(ek, R7, 6) ^ R8;

        /* R6 */
        R6 = flinv(ek, L7, 7);
        L6 = fo(ek, R6, 5) ^ flinv(ek, R7, 6);

        /* R5 */
        R5 = L6;
        L5 = fo(ek, R5, 4) ^ R6;

        /* R4 */
        R4 = flinv(ek, L5, 5);
        L4 = fo(ek, R4, 3) ^ flinv(ek, R5, 4);

        /* R3 */
        R3 = L4;
        L3 = fo(ek, R3, 2) ^ R4;

        /* R2 */
        R2 = flinv(ek, L3, 3);
        L2 = fo(ek, R2, 1) ^ flinv(ek, R3, 2);

        /* R1 */
        R1 = L2;
        L1 = fo(ek, R1, 0) ^ R2;

        /* R0 */
        R0 = flinv(ek, L1, 1);
        L0 = flinv(ek, R1, 0);

        ptext[0] = (char) ((L0 >> 24) & 0XFF);
        ptext[1] = (char) ((L0 >> 16) & 0XFF);
        ptext[2] = (char) ((L0 >> 8) & 0XFF);
        ptext[3] = (char) (L0 & 0XFF);
        ptext[4] = (char) ((R0 >> 24) & 0XFF);
        ptext[5] = (char) ((R0 >> 16) & 0XFF);
        ptext[6] = (char) ((R0 >> 8) & 0XFF);
        ptext[7] = (char) (R0 & 0XFF);
    }

}
