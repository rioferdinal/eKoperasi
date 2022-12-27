package com.example.ekoperasi;

public class URLs {

    private static final String ROOT_URL = "https://riodevlab.my.id/ekoperasi/";
//    private static final String ROOT_URL = "https://192.168.43.118/ekoperasi/";

    public static final String URL_REGISTER = ROOT_URL + "Api.php?apicall=signup";
    public static final String URL_LOGIN= ROOT_URL + "Api.php?apicall=login";
    public static final String URL_GET_KARYAWAN= ROOT_URL + "getKaryawan.php";
    public static final String URL_ADD_KARYAWAN= ROOT_URL + "addKaryawan.php";
    public static final String URL_GET_NASABAH= ROOT_URL + "getNasabah.php";
    public static final String URL_ADD_NASABAH= ROOT_URL + "addNasabah.php";
    public static final String URL_ADD_INCOME= ROOT_URL + "insertIncome.php";
    public static final String URL_GET_INCOME= ROOT_URL + "getIncome.php";
    public static final String URL_GET_REPORT= ROOT_URL + "getReport.php";
    public static final String URL_DEL_EMP= ROOT_URL + "deleteKaryawan.php";


}
