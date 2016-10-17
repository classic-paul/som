package som;

import java.util.Random;

/**
 *
 * @author Paul C Peacock <pcp1976@gmail.com>
 */
class Node{

    /**
     *
     */
    private int PVSize;
    private int FVSize;
    private double[] FV;
    private double[] PV;
    private int _X;
    private int _Y;

    Node(int FVSize, int PVSize, int Y, int X) {
        Random ran = new Random();
        this.FV = new double[FVSize];
        this.PV = new double[PVSize];

        for (int i = 0; i < FVSize; i++) {
            this.FV[i] = ran.nextDouble();
        }

        for (int i = 0; i < PVSize; i++) {
            this.PV[i] = ran.nextDouble();
        }
        this._X = X;
        this._Y = Y;
    }

    Node() {
        this(10, 10, 0, 0);
    }

    /**
     * @return the PVSize
     */
    int getPVSize() {
        return PVSize;
    }

    /**
     * @param PVSize the PVSize to set
     */
    void setPVSize(int PVSize) {
        this.PVSize = PVSize;
    }

    /**
     * @return the FVSize
     */
    int getFVSize() {
        return FVSize;
    }

    /**
     * @param FVSize the FVSize to set
     */
    void setFVSize(int FVSize) {
        this.FVSize = FVSize;
    }

    /**
     * @return the FV
     */
    double getFV(int index) {
        return FV[index];
    }

    /**
     * @return the FV
     */
    double[] getFV() {
        return FV;
    }

    /**
     * @param FV the FV to set
     */
    void setFV(double[] FV) {
        this.FV = FV;
    }

    void setFV(int index, double FV) {
        this.FV[index] = FV;
    }

    /**
     * @return the PV
     */
    double getPV(int index) {
        return PV[index];
    }

    /**
     * @return the PV
     */
    double[] getPV() {
        return PV;
    }

    /**
     * @param PV the PV to set
     */
    void setPV(double[] PV) {
        this.PV = PV;
    }

    void setPV(int index, double PV) {
        this.PV[index] = PV;
    }

    /**
     * @return the _X
     */
    int getX() {
        return _X;
    }

    /**
     * @param X the _X to set
     */
    void setX(int X) {
        this._X = X;
    }

    /**
     * @return the _Y
     */
    int getY() {
        return _Y;
    }

    /**
     * @param Y the _Y to set
     */
    void setY(int Y) {
        this._Y = Y;
    }
}
