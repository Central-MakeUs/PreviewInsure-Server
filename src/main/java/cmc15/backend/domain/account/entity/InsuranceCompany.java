package cmc15.backend.domain.account.entity;

import cmc15.backend.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static cmc15.backend.global.Result.FAIL;

@Getter
@AllArgsConstructor
public enum InsuranceCompany {

    SAMSUNG_LIFE("삼성생명", "https://direct.samsunglife.com/index.eds"),
    ABL_LIFE("ABL생명", "https://www.abllife.co.kr/"),
    AIA_LIFE("AIA생명", "https://dm.vitality.aia.co.kr/"),
    BNP_PARIBAS_CARDIF("BNP파리바카디프생명", "https://www.cardif.co.kr/"),
    DB_LIFE("DB생명", "https://direct.idblife.com/app/cm/mn/CMMN010010.do"),
    IM_LIFE("iM라이프", "https://www.dgbfnlife.com/main.do"),
    KB_LIFE("KB라이프생명", "https://www.kblife.co.kr/"),
    KDB_LIFE("KDB생명", "https://direct.kdblife.co.kr/main.do"),
    NH_NONGHYUP_LIFE("NH농협생명", "https://www.nhelife.co.kr/main.nhl"),
    KYOWO_LIFE_PLANET("교보라이프플래닛생명", "https://www.lifeplanet.co.kr/startmain/main.dev"),
    KYOWO_LIFE("교보생명", "https://www.kyobo.com/"),
    DONGYANG_LIFE("동양생명", "https://www.myangel.co.kr/process/main"),
    LINA_LIFE("라이나생명", "https://www.lina.co.kr/"),
    METLIFE("메트라이프생명", "https://www.emetlife.kr/"),
    MIRAEE_ASSET_LIFE("미래에셋생명", "https://life.miraeasset.com/home/index.do#MO-HO-000000-000000"),
    SHINHAN_LIFE("신한라이프생명", "https://www.shinhanlife.co.kr/hp/cdha0010.do"),
    CHUBB_LIFE("처브라이프생명", "https://www.chubblife.co.kr/index.do"),
    FUBON_HYUNDAI_LIFE("푸본현대생명", "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%91%E1%85%AE%E1%84%87%E1%85%A9%E1%86%AB%E1%84%92%E1%85%A7%E1%86%AB%E1%84%83%E1%85%A2%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png"),
    HANA_LIFE("하나생명", "https://hanalife.co.kr/"),
    HANHWA_LIFE("한화생명", "https://direct.hanwhalife.com/"),
    HEUNGKUK_LIFE("흥국생명", "https://www.heungkukdirect.com/main/0.html"),
    ETC("그 외", "none");


    private final String companyName;
    private final String url;

    public static String getUrlByCompanyName(String insuranceCompany) {
        for (InsuranceCompany company : values()) {
            if (company.getCompanyName().equals(insuranceCompany)) {
                return company.getUrl();
            }
        }
        throw new CustomException(FAIL);
    }
}
