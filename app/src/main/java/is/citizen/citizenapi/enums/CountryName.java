package is.citizen.citizenapi.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * ISO-3166 country code
 */
public enum CountryName
    {
    /**Afghanistan*/
    AF("Afghanistan"),
    /**Albania*/
    AL("Albania"),
    /**Algeria*/
    DZ("Algeria"),
    /**American Samoa*/
    AS("American Samoa"),
    /**Andorra*/
    AD("Andorra"),
    /**Angola*/
    AO("Angola"),
    /**Anguilla*/
    AI("Anguilla"),
    /**Antarctica*/
    AQ("Antarctica"),
    /**Antigua and Barbuda*/
    AG("Antigua and Barbuda"),
    /**Argentina*/
    AR("Argentina"),
    /**Armenia*/
    AM("Armenia"),
    /**Aruba*/
    AW("Aruba"),
    /**Australia*/
    AU("Australia"),
    /**Austria*/
    AT("Austria"),
    /**Azerbaijan*/
    AZ("Azerbaijan"),
    /**Bahamas*/
    BS("Bahamas"),
    /**Bahrain*/
    BH("Bahrain"),
    /**Bangladesh*/
    BD("Bangladesh"),
    /**Barbados*/
    BB("Barbados"),
    /**Belarus*/
    BY("Belarus"),
    /**Belgium*/
    BE("Belgium"),
    /**Belize*/
    BZ("Belize"),
    /**Benin*/
    BJ("Benin"),
    /**Bermuda*/
    BM("Bermuda"),
    /**Bhutan*/
    BT("Bhutan"),
    /**Bolivia*/
    BO("Bolivia"),
    /**Bonaire, Sint Eustatius and Saba*/
    BQ("Bonaire, Sint Eustatius and Saba"),
    /**Bosnia and Herzegovina*/
    BA("Bosnia and Herzegovina"),
    /**Botswana*/
    BW("Botswana"),
    /**Bouvet Island*/
    BV("Bouvet Island"),
    /**Brazil*/
    BR("Brazil"),
    /**British Indian Ocean Territory*/
    IO("British Indian Ocean Territory"),
    /**British Virgin Islands*/
    VG("British Virgin Islands"),
    /**Brunei*/
    BN("Brunei"),
    /**Bulgaria*/
    BG("Bulgaria"),
    /**Burkina Faso*/
    BF("Burkina Faso"),
    /**Burundi*/
    BI("Burundi"),
    /**Cambodia*/
    KH("Cambodia"),
    /**Cameroon*/
    CM("Cameroon"),
    /**Canada*/
    CA("Canada"),
    /**Cape Verde*/
    CV("Cape Verde"),
    /**Cayman Islands*/
    KY("Cayman Islands"),
    /**Central African Republic*/
    CF("Central African Republic"),
    /**Chad*/
    TD("Chad"),
    /**Chile*/
    CL("Chile"),
    /**China*/
    CN("China"),
    /**Christmas Island*/
    CX("Christmas Island"),
    /**Cocos Islands*/
    CC("Cocos Islands"),
    /**Colombia*/
    CO("Colombia"),
    /**Comoros*/
    KM("Comoros"),
    /**Congo*/
    CG("Congo"),
    /**Cook Islands*/
    CK("Cook Islands"),
    /**Costa Rica*/
    CR("Costa Rica"),
    /**Croatia*/
    HR("Croatia"),
    /**Cuba*/
    CU("Cuba"),
    /**Curaçao*/
    CW("Curaçao"),
    /**Cyprus*/
    CY("Cyprus"),
    /**Czech Republic*/
    CZ("Czech Republic"),
    /**Côte d'Ivoire*/
    CI("Côte d'Ivoire"),
    /**Denmark*/
    DK("Denmark"),
    /**Djibouti*/
    DJ("Djibouti"),
    /**Dominica*/
    DM("Dominica"),
    /**Dominican Republic*/
    DO("Dominican Republic"),
    /**Ecuador*/
    EC("Ecuador"),
    /**Egypt*/
    EG("Egypt"),
    /**El Salvador*/
    SV("El Salvador"),
    /**Equatorial Guinea*/
    GQ("Equatorial Guinea"),
    /**Eritrea*/
    ER("Eritrea"),
    /**Estonia*/
    EE("Estonia"),
    /**Ethiopia*/
    ET("Ethiopia"),
    /**Falkland Islands*/
    FK("Falkland Islands"),
    /**Faroe Islands*/
    FO("Faroe Islands"),
    /**Fiji*/
    FJ("Fiji"),
    /**Finland*/
    FI("Finland"),
    /**France*/
    FR("France"),
    /**French Guiana*/
    GF("French Guiana"),
    /**French Polynesia*/
    PF("French Polynesia"),
    /**French Southern Territories*/
    TF("French Southern Territories"),
    /**Gabon*/
    GA("Gabon"),
    /**Gambia*/
    GM("Gambia"),
    /**Georgia*/
    GE("Georgia"),
    /**Germany*/
    DE("Germany"),
    /**Ghana*/
    GH("Ghana"),
    /**Gibraltar*/
    GI("Gibraltar"),
    /**Greece*/
    GR("Greece"),
    /**Greenland*/
    GL("Greenland"),
    /**Grenada*/
    GD("Grenada"),
    /**Guadeloupe*/
    GP("Guadeloupe"),
    /**Guam*/
    GU("Guam"),
    /**Guatemala*/
    GT("Guatemala"),
    /**Guernsey*/
    GG("Guernsey"),
    /**Guinea*/
    GN("Guinea"),
    /**Guinea-Bissau*/
    GW("Guinea-Bissau"),
    /**Guyana*/
    GY("Guyana"),
    /**Haiti*/
    HT("Haiti"),
    /**Heard Island And McDonald Islands*/
    HM("Heard Island And McDonald Islands"),
    /**Honduras*/
    HN("Honduras"),
    /**Hong Kong*/
    HK("Hong Kong"),
    /**Hungary*/
    HU("Hungary"),
    /**Iceland*/
    IS("Iceland"),
    /**India*/
    IN("India"),
    /**Indonesia*/
    ID("Indonesia"),
    /**Iran*/
    IR("Iran"),
    /**Iraq*/
    IQ("Iraq"),
    /**Ireland*/
    IE("Ireland"),
    /**Isle Of Man*/
    IM("Isle Of Man"),
    /**Israel*/
    IL("Israel"),
    /**Italy*/
    IT("Italy"),
    /**Jamaica*/
    JM("Jamaica"),
    /**Japan*/
    JP("Japan"),
    /**Jersey*/
    JE("Jersey"),
    /**Jordan*/
    JO("Jordan"),
    /**Kazakhstan*/
    KZ("Kazakhstan"),
    /**Kenya*/
    KE("Kenya"),
    /**Kiribati*/
    KI("Kiribati"),
    /**Kuwait*/
    KW("Kuwait"),
    /**Kyrgyzstan*/
    KG("Kyrgyzstan"),
    /**Laos*/
    LA("Laos"),
    /**Latvia*/
    LV("Latvia"),
    /**Lebanon*/
    LB("Lebanon"),
    /**Lesotho*/
    LS("Lesotho"),
    /**Liberia*/
    LR("Liberia"),
    /**Libya*/
    LY("Libya"),
    /**Liechtenstein*/
    LI("Liechtenstein"),
    /**Lithuania*/
    LT("Lithuania"),
    /**Luxembourg*/
    LU("Luxembourg"),
    /**Macao*/
    MO("Macao"),
    /**Macedonia*/
    MK("Macedonia"),
    /**Madagascar*/
    MG("Madagascar"),
    /**Malawi*/
    MW("Malawi"),
    /**Malaysia*/
    MY("Malaysia"),
    /**Maldives*/
    MV("Maldives"),
    /**Mali*/
    ML("Mali"),
    /**Malta*/
    MT("Malta"),
    /**Marshall Islands*/
    MH("Marshall Islands"),
    /**Martinique*/
    MQ("Martinique"),
    /**Mauritania*/
    MR("Mauritania"),
    /**Mauritius*/
    MU("Mauritius"),
    /**Mayotte*/
    YT("Mayotte"),
    /**Mexico*/
    MX("Mexico"),
    /**Micronesia*/
    FM("Micronesia"),
    /**Moldova*/
    MD("Moldova"),
    /**Monaco*/
    MC("Monaco"),
    /**Mongolia*/
    MN("Mongolia"),
    /**Montenegro*/
    ME("Montenegro"),
    /**Montserrat*/
    MS("Montserrat"),
    /**Morocco*/
    MA("Morocco"),
    /**Mozambique*/
    MZ("Mozambique"),
    /**Myanmar*/
    MM("Myanmar"),
    /**Namibia*/
    NA("Namibia"),
    /**Nauru*/
    NR("Nauru"),
    /**Nepal*/
    NP("Nepal"),
    /**Netherlands*/
    NL("Netherlands"),
    /**Netherlands Antilles*/
    AN("Netherlands Antilles"),
    /**New Caledonia*/
    NC("New Caledonia"),
    /**New Zealand*/
    NZ("New Zealand"),
    /**Nicaragua*/
    NI("Nicaragua"),
    /**Niger*/
    NE("Niger"),
    /**Nigeria*/
    NG("Nigeria"),
    /**Niue*/
    NU("Niue"),
    /**Norfolk Island*/
    NF("Norfolk Island"),
    /**North Korea*/
    KP("North Korea"),
    /**Northern Mariana Islands*/
    MP("Northern Mariana Islands"),
    /**Norway*/
    NO("Norway"),
    /**Oman*/
    OM("Oman"),
    /**Pakistan*/
    PK("Pakistan"),
    /**Palau*/
    PW("Palau"),
    /**Palestine*/
    PS("Palestine"),
    /**Panama*/
    PA("Panama"),
    /**Papua New Guinea*/
    PG("Papua New Guinea"),
    /**Paraguay*/
    PY("Paraguay"),
    /**Peru*/
    PE("Peru"),
    /**Philippines*/
    PH("Philippines"),
    /**Pitcairn*/
    PN("Pitcairn"),
    /**Poland*/
    PL("Poland"),
    /**Portugal*/
    PT("Portugal"),
    /**Puerto Rico*/
    PR("Puerto Rico"),
    /**Qatar*/
    QA("Qatar"),
    /**Reunion*/
    RE("Reunion"),
    /**Romania*/
    RO("Romania"),
    /**Russia*/
    RU("Russia"),
    /**Rwanda*/
    RW("Rwanda"),
    /**Saint Barthélemy*/
    BL("Saint Barthélemy"),
    /**Saint Helena*/
    SH("Saint Helena"),
    /**Saint Kitts And Nevis*/
    KN("Saint Kitts And Nevis"),
    /**Saint Lucia*/
    LC("Saint Lucia"),
    /**Saint Martin*/
    MF("Saint Martin"),
    /**Saint Pierre And Miquelon*/
    PM("Saint Pierre And Miquelon"),
    /**Saint Vincent And The Grenadines*/
    VC("Saint Vincent And The Grenadines"),
    /**Samoa*/
    WS("Samoa"),
    /**San Marino*/
    SM("San Marino"),
    /**Sao Tome And Principe*/
    ST("Sao Tome And Principe"),
    /**Saudi Arabia*/
    SA("Saudi Arabia"),
    /**Senegal*/
    SN("Senegal"),
    /**Serbia*/
    RS("Serbia"),
    /**Seychelles*/
    SC("Seychelles"),
    /**Sierra Leone*/
    SL("Sierra Leone"),
    /**Singapore*/
    SG("Singapore"),
    /**Sint Maarten (Dutch part)*/
    SX("Sint Maarten (Dutch part)"),
    /**Slovakia*/
    SK("Slovakia"),
    /**Slovenia*/
    SI("Slovenia"),
    /**Solomon Islands*/
    SB("Solomon Islands"),
    /**Somalia*/
    SO("Somalia"),
    /**South Africa*/
    ZA("South Africa"),
    /**South Georgia And The South Sandwich Islands*/
    GS("South Georgia And The South Sandwich Islands"),
    /**South Korea*/
    KR("South Korea"),
    /**South Sudan*/
    SS("South Sudan"),
    /**Spain*/
    ES("Spain"),
    /**Sri Lanka*/
    LK("Sri Lanka"),
    /**Sudan*/
    SD("Sudan"),
    /**Suriname*/
    SR("Suriname"),
    /**Svalbard And Jan Mayen*/
    SJ("Svalbard And Jan Mayen"),
    /**Swaziland*/
    SZ("Swaziland"),
    /**Sweden*/
    SE("Sweden"),
    /**Switzerland*/
    CH("Switzerland"),
    /**Syria*/
    SY("Syria"),
    /**Taiwan*/
    TW("Taiwan"),
    /**Tajikistan*/
    TJ("Tajikistan"),
    /**Tanzania*/
    TZ("Tanzania"),
    /**Thailand*/
    TH("Thailand"),
    /**The Democratic Republic Of Congo*/
    CD("The Democratic Republic Of Congo"),
    /**Timor-Leste*/
    TL("Timor-Leste"),
    /**Togo*/
    TG("Togo"),
    /**Tokelau*/
    TK("Tokelau"),
    /**Tonga*/
    TO("Tonga"),
    /**Trinidad and Tobago*/
    TT("Trinidad and Tobago"),
    /**Tunisia*/
    TN("Tunisia"),
    /**Turkey*/
    TR("Turkey"),
    /**Turkmenistan*/
    TM("Turkmenistan"),
    /**Turks And Caicos Islands*/
    TC("Turks And Caicos Islands"),
    /**Tuvalu*/
    TV("Tuvalu"),
    /**U.S. Virgin Islands*/
    VI("U.S. Virgin Islands"),
    /**Uganda*/
    UG("Uganda"),
    /**Ukraine*/
    UA("Ukraine"),
    /**United Arab Emirates*/
    AE("United Arab Emirates"),
    /**United Kingdom*/
    GB("United Kingdom"),
    /**United States*/
    US("United States"),
    /**United States Minor Outlying Islands*/
    UM("United States Minor Outlying Islands"),
    /**Uruguay*/
    UY("Uruguay"),
    /**Uzbekistan*/
    UZ("Uzbekistan"),
    /**Vanuatu*/
    VU("Vanuatu"),
    /**Vatican*/
    VA("Vatican"),
    /**Venezuela*/
    VE("Venezuela"),
    /**Vietnam*/
    VN("Vietnam"),
    /**Wallis And Futuna*/
    WF("Wallis And Futuna"),
    /**Western Sahara*/
    EH("Western Sahara"),
    /**Yemen*/
    YE("Yemen"),
    /**Zambia*/
    ZM("Zambia"),
    /**Zimbabwe*/
    ZW("Zimbabwe"),
    /**Åland Islands*/
    AX("Åland Islands");

    /**
     * Dialing code for country
     */
    private String name;

    /**
     * Static map for lookup later
     */
    private static final Map<String, CountryName> COUNTRY_NAME_MAP = new HashMap<String, CountryName>();

    /**
     * Builds map
     */
    static
        {
        for (CountryName countryName : values())
            {
            COUNTRY_NAME_MAP.put(countryName.name(), countryName);
            }
        }

    /**
     * Create new instance
     *
     * @param name country name
     */
    private CountryName(String name)
        {
        this.name = name;
        }

    /**
     * To support mixed case Country Code
     *
     * @param value country code
     * @return {@code CountryCode}
     */
    public static CountryName fromValue(String value)
        {
        // Since Country code is mandatory only in context of an address
        if (value == null || value.trim().length() == 0)
            {
            return null;
            }
        String trimmedString = value.replaceAll("\\s+", "");
        final CountryName countryName = COUNTRY_NAME_MAP.get(trimmedString.toUpperCase());
        if (countryName == null)
            {
            throw new IllegalArgumentException("Invalid Country Code");
            }
        return countryName;
        }

    /**
     * Get name of country
     *
     * @return namefor country
     */
    public String getName()
        {
        return name;
        }


    }
