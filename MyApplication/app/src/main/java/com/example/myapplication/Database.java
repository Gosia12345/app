package com.example.myapplication;

public class Database {
    /*kolumny tabeli*/
    private Integer ID;

    private String ACC_X;
    private Double ACC_Y;
    private Double ACC_Z;

    private Double MAG_X;
    private Double MAG_Y;
    private Double MAG_Z;

    private Double ORI_X;
    private Double ORI_Y;
    private Double ORI_Z;

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public String getACCX() {
        return ACC_X;
    }
    public Double getACCY() {
        return ACC_Y;
    }
    public Double getACCZ() {
        return ACC_Z;
    }
    public Double getMAGX() {
        return MAG_X;
    }
    public Double getMAGY() {
        return MAG_Y;
    }
    public Double getMAGZ() {
        return MAG_Z;
    }
    public Double getORIX() {
        return ORI_X;
    }
    public Double getORIY() {
        return ORI_Y;
    }
    public Double getORIZ() {
        return ORI_Z;
    }

    public void setACCX() {
        this.ACC_X = ACC_X;
    }
    public void setACCY() {
        this.ACC_Y = ACC_Y;
    }
    public void setACCZ() {
        this.ACC_Z = ACC_Z;
    }
    public void setMAGX() {
        this.MAG_X = MAG_X;
    }
    public void setMAGY() {
        this.MAG_Y = MAG_Y;
    }
    public void setMAGZ() {
        this.MAG_Z = MAG_Z;
    }
    public void setORIX() {
        this.ORI_X = ORI_X;
    }
    public void setORIY() {
        this.ORI_Y = ORI_Y;
    }
    public void setORIZ() {
        this.ORI_Z = ORI_Z;
    }

        public Database() { }
        public Database(int ID,
                        String ACC_X,
                        Double ACC_Y,
                        Double ACC_Z,
                        Double MAG_X,
                        Double MAG_Y,
                        Double MAG_Z,
                        Double ORI_X,
                        Double ORI_Y,
                        Double ORI_Z
                        ) {
            this.ID = ID;
            this.ACC_X = ACC_X;
            this.ACC_Y = ACC_Y;
            this.ACC_Z = ACC_Z;
            this.MAG_X = MAG_X;
            this.MAG_Y = MAG_Y;
            this.MAG_Z = MAG_Z;
            this.ORI_X = ORI_X;
            this.ORI_Y = ORI_Y;
            this.ORI_Z = ORI_Z;
        }

    }