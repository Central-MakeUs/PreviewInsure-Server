package cmc15.backend.domain.insurance.entity;

import lombok.Getter;

@Getter
public enum InsuranceCompanyImage {

    SAMSUNG_LIFE("삼성생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%89%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A5%E1%86%BC%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    ABL_LIFE("ABL생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/ABL%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    AIA_LIFE("AIA생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/AIA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    BNP_PARIBAS_CARDIF("BNP파리바카디프생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/BNP.png"),
    DB_LIFE("DB생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/DB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    IM_LIFE("iM라이프", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/iM%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png"),
    KB_LIFE("KB라이프생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/KB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC%E1%84%87%E1%85%A9%E1%84%92%E1%85%A5%E1%86%B7.png"),
    KDB_LIFE("KDB생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/KDB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    NH_NONGHYUP_LIFE("NH농협생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/NH%E1%84%82%E1%85%A9%E1%86%BC%E1%84%92%E1%85%A7%E1%86%B8%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    KYOWO_LIFE_PLANET("교보라이프플래닛생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%80%E1%85%AD%E1%84%87%E1%85%A9%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A2%E1%84%82%E1%85%B5%E1%86%BA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    KYOWO_LIFE("교보생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%80%E1%85%AD%E1%84%87%E1%85%A9%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    DONGYANG_LIFE("동양생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%BC%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    LINA_LIFE("라이나생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%82%E1%85%A1%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    METLIFE("메트라이프생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%86%E1%85%B5%E1%84%83%E1%85%B3%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png"),
    MIRAEE_ASSET_LIFE("미래에셋생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%86%E1%85%B5%E1%84%85%E1%85%A2%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A6%E1%86%BA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    SHINHAN_LIFE("신한라이프생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%89%E1%85%B5%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AB%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png"),
    CHUBB_LIFE("처브라이프생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/CHUBB.png"),
    FUBON_HYUNDAI_LIFE("푸본현대생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%91%E1%85%AE%E1%84%87%E1%85%A9%E1%86%AB%E1%84%92%E1%85%A7%E1%86%AB%E1%84%83%E1%85%A2%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    HANA_LIFE("하나생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%A1%E1%84%82%E1%85%A1%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    HANHWA_LIFE("한화생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%A1%E1%86%AB%E1%84%92%E1%85%AA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    HEUNGKUK_LIFE("흥국생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%B3%E1%86%BC%E1%84%80%E1%85%AE%E1%86%A8%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png");


    private final String companyName;
    private final String imageUrl;

    InsuranceCompanyImage(String companyName, String imageUrl) {
        this.companyName = companyName;
        this.imageUrl = imageUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static String getImageUrlByCompanyName(String companyName) {
        for (InsuranceCompanyImage company : values()) {
            if (company.getCompanyName().equals(companyName)) {
                return company.getImageUrl();
            }
        }
        return "";
    }
}

