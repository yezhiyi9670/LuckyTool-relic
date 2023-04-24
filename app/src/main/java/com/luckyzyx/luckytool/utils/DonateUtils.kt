package com.luckyzyx.luckytool.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.LayoutDonateItemBinding
import java.io.Serializable
import java.text.DecimalFormat

@Suppress("PrivatePropertyName")
class DonateData(val context: Context) {
    private val CNU = "NULL"
    private val CQQ = "QQ"
    private val CQQHB = "QQHB"
    private val CWC = "WeChat"
    private val CAP = "AliPay"
    private val CPP = "PayPal"
    private fun getDonateData(): ArrayList<DInfo> {
        return ArrayList<DInfo>().apply {
            add(
                DInfo(
                    "是小奶糖啊", details(
                        DCInfo("20220912-003911", CWC, 1.0, "10000499012022091201251965341571"),
                        DCInfo("20220912-003928", CWC, 1.0, "10001071012022091201925356060626"),
                        DCInfo("20220912-003952", CWC, 1.0, "10000499012022091201394232947529"),
                        DCInfo("20220912-004010", CWC, 1.0, "10000499012022091201117667651586"),
                        DCInfo("20220912-004034", CWC, 1.0, "10001071012022091201573323684619"),
                        DCInfo("20220912-004102", CWC, 1.0, "10000499012022091201061912891540"),
                        DCInfo("20220912-004120", CWC, 1.0, "10000499012022091201695371491603"),
                        DCInfo("20220912-004138", CWC, 1.0, "10000499012022091201029478590602"),
                        DCInfo("20220912-004158", CWC, 1.0, "10000499012022091201421833786544"),
                        DCInfo("20220912-004216", CWC, 1.0, "10000499012022091201781561301536"),
                        DCInfo("20220912-230934", CWC, 1.0, "10000499012022091201534268361646"),
                        DCInfo("20220912-231016", CWC, 1.0, "10001071012022091201531626564574"),
                        DCInfo("20220912-231043", CWC, 1.0, "10001071012022091201628715912641"),
                        DCInfo("20220912-231103", CWC, 1.0, "10000499012022091201917012382639"),
                        DCInfo("20220912-231130", CWC, 1.0, "10001071012022091201327282039502"),
                        DCInfo("20220915-005135", CAP, 0.1, "2022091522001418391439574777"),
                        DCInfo("20220915-005314", CAP, 0.2, "2022091522001418391438389614"),
                        DCInfo("20220915-005343", CAP, 0.3, "2022091522001418391439574778"),
                        DCInfo("20220915-005408", CAP, 0.4, "2022091522001418391438987850"),
                        DCInfo("20220915-005915", CAP, 5.0, "2022091522001418391438398792"),
                        DCInfo("20230125-145957", CQQ, 11.0, "101000026901302301251417959764"),
                        DCInfo("20230404-182753", CQQ, 0.05, "101000026901302304041418596282")
                    )
                )
            )
            add(
                DInfo(
                    "Kimjaejiang", details(
                        DCInfo("20220912-205238", CWC, 5.0, "10000499012022091200755856699085")
                    )
                )
            )
            add(
                DInfo(
                    "*耿", details(
                        DCInfo("20220914-083334", CWC, 1.0, "10001071012022091401684644133513")
                    )
                )
            )
            add(
                DInfo(
                    "邹王", details(
                        DCInfo("20220916-233422", CQQ, 15.0, "101000026901502209161435139989"),
                        DCInfo(
                            "20221110-103510",
                            CQQHB,
                            51.66,
                            "1000032001221110360010904620380000"
                        )
                    )
                )
            )
            add(
                DInfo(
                    "*す", details(
                        DCInfo("20220919-235632", CWC, 20.0, "10001071012022091900284575580158")
                    )
                )
            )
            add(
                DInfo(
                    "楠", details(
                        DCInfo("20220925-204804", CAP, 10.0, "2022092522001495601447752596")
                    )
                )
            )
            add(
                DInfo(
                    "天伞桜", details(
                        DCInfo("20220926-083025", CAP, 88.8, "2022092622001422141429313950")
                    )
                )
            )
            add(
                DInfo(
                    "*伟", details(
                        DCInfo("20220930-232126", CAP, 15.0, "2022093022001491561432064831")
                    )
                )
            )
            add(
                DInfo(
                    "ssd·风格", details(
                        DCInfo("20221012-203606", CAP, 5.0, "2022101222001475911409839972")
                    )
                )
            )
            add(
                DInfo(
                    "**智", details(
                        DCInfo("20221013-072635", CAP, 15.0, "2022101322001464631446819251")
                    )
                )
            )
            add(
                DInfo(
                    "*恩", details(
                        DCInfo("20221013-113220", CAP, 10.0, "2022101322001401321434146632")
                    )
                )
            )
            add(
                DInfo(
                    "佘樂", details(
                        DCInfo("20221017-090951", CWC, 20.0, "10001071012022101700116472524081")
                    )
                )
            )
            add(
                DInfo(
                    "风冷涂的蜡", details(
                        DCInfo("20221018-131217", CQQ, 10.0, "101000026901502210181449517794")
                    )
                )
            )
            add(
                DInfo(
                    "**才", details(
                        DCInfo("20221018-122213", CAP, 10.0, "2022101822001424721421267656")
                    )
                )
            )
            add(
                DInfo(
                    "*灯", details(
                        DCInfo("20221019-195159", CWC, 5.0, "10000499012022101901514527492524")
                    )
                )
            )
            add(
                DInfo(
                    "*G", details(
                        DCInfo("20221023-083920", CWC, 66.0, "10001071012022102300250132780051")
                    )
                )
            )
            add(
                DInfo(
                    "*荣", details(
                        DCInfo("20221023-082337", CAP, 8.8, "2022102322001418341416028048")
                    )
                )
            )
            add(
                DInfo(
                    "斯已", details(
                        DCInfo("20221025-232101", CAP, 15.0, "2022102522001476341430603222")
                    )
                )
            )
            add(
                DInfo(
                    "冰梦", details(
                        DCInfo("20221028-122652", CAP, 20.0, "2022102822001449721407673099")
                    )
                )
            )
            add(
                DInfo(
                    "*?", details(
                        DCInfo("20221029-204415", CWC, 8.8, "10001071012022102900604818941128")
                    )
                )
            )
            add(
                DInfo(
                    "晨钟酱", details(
                        DCInfo("20221102-082807", CWC, 100.0, "10000499012022110200766818668071")
                    )
                )
            )
            add(
                DInfo(
                    "小李.", details(
                        DCInfo("20221103", CAP, 15.0, "2022110322001471241434100859")
                    )
                )
            )
            add(
                DInfo(
                    "yomol.", details(
                        DCInfo("20221109-174640", CWC, 18.0, "10001071012022110901232470547613")
                    )
                )
            )
            add(
                DInfo(
                    "miComet35P", details(
                        DCInfo("20221110-153627", CAP, 10.0, "2022111022001402571439794753")
                    )
                )
            )
            add(
                DInfo(
                    "347aidan", details(
                        DCInfo("20221111-082228", CWC, 5.0, "10000499012022111101636288086525")
                    )
                )
            )
            add(
                DInfo(
                    "柳丝", details(
                        DCInfo("20221111-162944", CQQ, 10.0, "101000026901302211111384241371")
                    )
                )
            )
            add(
                DInfo(
                    "*鱼", details(
                        DCInfo("20221115-123753", CWC, 5.0, "10001071012022111501267896555529")
                    )
                )
            )
            add(
                DInfo(
                    "佘小鱼", details(
                        DCInfo("20221115-124129", CAP, 21.1, "2022111522001494811407014490"),
                        DCInfo("20230121-100513", CAP, 10.0, "2023012122001494811424220936")
                    )
                )
            )
            add(
                DInfo(
                    "**程", details(
                        DCInfo("20221119-134103", CAP, 20.0, "2022111922001416771433945337")
                    )
                )
            )
            add(
                DInfo(
                    "miro", details(
                        DCInfo("20221122-232621", CQQ, 3.0, "101000026901302211221386081290")
                    )
                )
            )
            add(
                DInfo(
                    "你会振刀么", details(
                        DCInfo("20221123-005251", CWC, 18.88, "10001071012022112301342736297502"),
                        DCInfo("20230401-122607", CWC, 32.0, "10001071012023040101014984671581"),
                        DCInfo("20230401-123543", CWC, 1.12, "10000499012023040101074692618527"),
                        DCInfo("20230401-123605", CWC, 18.88, "10001071012023040101300514212581")
                    )
                )
            )
            add(
                DInfo(
                    "大挪移", details(
                        DCInfo("20221123-203918", CWC, 20.0, "10000499012022112300516761031117")
                    )
                )
            )
            add(
                DInfo(
                    "**林", details(
                        DCInfo("20221125-093459", CAP, 10.0, "2022112522001476991437614992")
                    )
                )
            )
            add(
                DInfo(
                    "*林", details(
                        DCInfo("20221128-030319", CWC, 10.0, "10000499012022112801909258499610")
                    )
                )
            )
            add(
                DInfo(
                    "*₂", details(
                        DCInfo("20221130-231036", CWC, 6.0, "10000499012022113001824358393615")
                    )
                )
            )
            add(
                DInfo(
                    "无", details(
                        DCInfo("20221202-162017", CQQ, 5.0, "101000026901302212021417779297")
                    )
                )
            )
            add(
                DInfo(
                    "Mr.x", details(
                        DCInfo("20221204-192602", CWC, 20.0, "10000499012022120400706169995060")
                    )
                )
            )
            add(
                DInfo(
                    "小妤", details(
                        DCInfo("20221202-034008", CAP, 13.14, "2022120222001496361401339630")
                    )
                )
            )
            add(
                DInfo(
                    "*方", details(
                        DCInfo("20221204-230824", CWC, 5.0, "10001071012022120401935056538617")
                    )
                )
            )
            add(
                DInfo(
                    "渐渐简单", details(
                        DCInfo("20221205-054441", CWC, 5.0, "10001071012022120500559363563095")
                    )
                )
            )
            add(
                DInfo(
                    "㗽翳鮦峃", details(
                        DCInfo("20221210-163315", CAP, 6.66, "2022121022001419861424041467")
                    )
                )
            )
            add(
                DInfo(
                    "Steven", details(
                        DCInfo("20221210-235500", CAP, 20.0, "2022121022001456961433458318")
                    )
                )
            )
            add(
                DInfo(
                    "缺德导航", details(
                        DCInfo("20221212-144051", CAP, 15.0, "2022121222001415271423722807")
                    )
                )
            )
            add(
                DInfo(
                    "萝卜啊", details(
                        DCInfo("20221219-143133", CWC, 10.0, "10000499012022121901553619615591")
                    )
                )
            )
            add(
                DInfo(
                    "琉星", details(
                        DCInfo("20221222-184315", CWC, 6.66, "10001071012022122201044935883530")
                    )
                )
            )
            add(
                DInfo(
                    "鸿鹄1207", details(
                        DCInfo("20221226-200021", CWC, 3.0, "10001071012022122601752872360502")
                    )
                )
            )
            add(
                DInfo(
                    "ddddk", details(
                        DCInfo("20221229-225345", CWC, 5.0, "10001071012022122901900634415576")
                    )
                )
            )
            add(
                DInfo(
                    "He_zhCN", details(
                        DCInfo("20230110-220546", CAP, 5.0, "2023011022001440131458365074")
                    )
                )
            )
            add(
                DInfo(
                    "panfung", details(
                        DCInfo("20230111-181655", CAP, 18.0, "2023011122001412191410136732")
                    )
                )
            )
            add(
                DInfo(
                    "G.Sheng", details(
                        DCInfo("20230117-120623", CWC, 25.0, "10001071012023011700412830034162")
                    )
                )
            )
            add(
                DInfo(
                    "故里", details(
                        DCInfo("20230119-002610", CQQHB, 5.0, "1000046601230119390010781395540000")
                    )
                )
            )
            add(
                DInfo(
                    "散乱的钟", details(
                        DCInfo("20230119-152310", CWC, 6.8, "10001071012023011901412388984600")
                    )
                )
            )
            add(
                DInfo(
                    "项欣", details(
                        DCInfo("20230121-170458", CWC, 1.0, "10000499012023012101792786463501")
                    )
                )
            )
            add(
                DInfo(
                    "我是大笨熊", details(
                        DCInfo("20230125-061251", CQQ, 20.0, "101000026901502301251436803564")
                    )
                )
            )
            add(
                DInfo(
                    "鱼知乐", details(
                        DCInfo("20230125-094400", CAP, 20.0, "2023012522001424271419420653"),
                        DCInfo("20230401-165807", CAP, 20.0, "2023040122001424271448335326")
                    )
                )
            )
            add(
                DInfo(
                    "高木同学咩", details(
                        DCInfo("20230126-000631", CWC, 8.8, "10001071012023012600559933304093")
                    )
                )
            )
            add(
                DInfo(
                    "是麦芽糖呀", details(
                        DCInfo("20230126-005542", CWC, 13.14, "10001071012023012600871793553057"),
                        DCInfo("20230315-024015", CWC, 5.0, "10001071012023031500985324142171"),
                        DCInfo("20230420-220602", CWC, 0.5, "10001071012023042000824782126077"),
                    )
                )
            )
            add(
                DInfo(
                    "贱贱", details(
                        DCInfo("20230126-124451", CWC, 8.88, "10000499012023012600637975717166")
                    )
                )
            )
            add(
                DInfo(
                    "一杯美式", details(
                        DCInfo("20230129-092919", CWC, 1.3, "10001071012023012900425726231103")
                    )
                )
            )
            add(
                DInfo(
                    "加两个卤蛋", details(
                        DCInfo("20230205-032712", CWC, 5.0, "10000499012023020501552678244628")
                    )
                )
            )
            add(
                DInfo(
                    "Zhang_ZL", details(
                        DCInfo("20230206-134646", CWC, 10.0, "10000499012023020601408947796566")
                    )
                )
            )
            add(
                DInfo(
                    "容声", details(
                        DCInfo("20230206-180607", CWC, 11.11, "10000499012023020600302469760124")
                    )
                )
            )
            add(
                DInfo(
                    "奥利奥", details(
                        DCInfo("20230207-074804", CQQ, 88.88, "101000026901302302071390154975")
                    )
                )
            )
            add(
                DInfo(
                    "xunone", details(
                        DCInfo("20230209-220346", CQQ, 64.0, "10001071012023020901162404474545")
                    )
                )
            )
            add(
                DInfo(
                    "XiaMi1244", details(
                        DCInfo("20230210-214507", CWC, 1.88, "10001071012023021001895877477512")
                    )
                )
            )
            add(
                DInfo(
                    "痕祭", details(
                        DCInfo("20230212-233213", CQQ, 10.0, "101000026901302302121391049274")
                    )
                )
            )
            add(
                DInfo(
                    "Sakura酱", details(
                        DCInfo("20230216-140538", CAP, 15.0, "2023021622001490101430998193")
                    )
                )
            )
            add(
                DInfo(
                    "kjhiorv", details(
                        DCInfo("20230220-215726", CWC, 5.0, "10001071012023022000751417859195")
                    )
                )
            )
            add(
                DInfo(
                    "*宝", details(
                        DCInfo("20230222-001142", CWC, 6.66, "10000499012023022200978312050171")
                    )
                )
            )
            add(
                DInfo(
                    "滑了一地稽", details(
                        DCInfo("20230224-114332", CAP, 1.88, "2023022422001451621443701099")
                    )
                )
            )
            add(
                DInfo(
                    "*淼", details(
                        DCInfo("20230224-211916", CAP, 30.0, "2023022422001419001450383135")
                    )
                )
            )
            add(
                DInfo(
                    "教父", details(
                        DCInfo("20230225-005415", CWC, 6.66, "10000499012023022500189770135110")
                    )
                )
            )
            add(
                DInfo(
                    "尘", details(
                        DCInfo("20230225-150953", CWC, 10.0, "10000499012023022501911668467588")
                    )
                )
            )
            add(
                DInfo(
                    "Hoyice", details(
                        DCInfo("20230227-194215", CAP, 6.66, "2023022722001429031439440847")
                    )
                )
            )
            add(
                DInfo(
                    "*4168", details(
                        DCInfo("20230309-172855", CWC, 10.0, "10000499012023030900093719416185")
                    )
                )
            )
            add(
                DInfo(
                    "*晚", details(
                        DCInfo("20230310-011819", CWC, 0.66, "10001071012023031001733875940534")
                    )
                )
            )
            add(
                DInfo(
                    "*贤", details(
                        DCInfo("20230312-111917", CWC, 28.88, "10001071012023031201740580863601")
                    )
                )
            )
            add(
                DInfo(
                    "徐悲鸿", details(
                        DCInfo("20230316-144330", CWC, 5.2, "10000499012023031601034829603616")
                    )
                )
            )
            add(
                DInfo(
                    "纯鹿人", details(
                        DCInfo("20230317-081532", CWC, 3.17, "10001071012023031701213594856602")
                    )
                )
            )
            add(
                DInfo(
                    "ken995", details(
                        DCInfo("20230317-094837", CWC, 10.0, "10001071012023031701981563932600")
                    )
                )
            )
            add(
                DInfo(
                    "ANXIOUS", details(
                        DCInfo("20230325-181416", CWC, 10.0, "10000499012023032501897349857593")
                    )
                )
            )
            add(
                DInfo(
                    "sxwzxc", details(
                        DCInfo("20230327-160730", CAP, 1.88, "2023032722001461361413019639")
                    )
                )
            )
            add(
                DInfo(
                    "Ba7nice", details(
                        DCInfo("20230329-184445", CQQ, 9.8, "101000026901302303291397612548")
                    )
                )
            )
            add(
                DInfo(
                    "剪贴板", details(
                        DCInfo("20230330-174939", CAP, 5.0, "2023033022001467461412836642")
                    )
                )
            )
            add(
                DInfo(
                    "Aixmos", details(
                        DCInfo("20230404-135851", CWC, 5.0, "10000499012023040400997206766054")
                    )
                )
            )
            add(
                DInfo(
                    "不忘初心", details(
                        DCInfo("20230404-182211", CQQ, 10.0, "101000026901302304041418595239")
                    )
                )
            )
            add(
                DInfo(
                    "Li", details(
                        DCInfo("20230404-183814", CWC, 10.0, "10000499012023040401707385444566")
                    )
                )
            )
            add(
                DInfo(
                    "云啊", details(
                        DCInfo("20230406-004130", CWC, 66.66, "10000499012023040601029646275565")
                    )
                )
            )
            add(
                DInfo(
                    "感谢", details(
                        DCInfo("20230406-094249", CAP, 1.0, "2023040622001468391412654936")
                    )
                )
            )
            add(
                DInfo(
                    "雲", details(
                        DCInfo("20230408-215743", CWC, 10.0, "10000499012023040801401660211557")
                    )
                )
            )
            add(
                DInfo(
                    "零落", details(
                        DCInfo("20230409-002621", CWC, 1.0, "10000499012023040901443033591636"),
                        DCInfo("20230411-134410", CWC, 4.0, "10001071012023041101344473117559"),
                        DCInfo("20230411-182708", CWC, 8.88, "10000499012023041101969609525589")
                    )
                )
            )
            add(
                DInfo(
                    "某条咸鱼SS", details(
                        DCInfo("20230411-210021", CWC, 10.0, "10000499012023041100484506704192")
                    )
                )
            )
            add(
                DInfo(
                    "soulbring", details(
                        DCInfo("20230411-210222", CWC, 10.0, "10001071012023041100165801723174")
                    )
                )
            )
            add(
                DInfo(
                    "Raf", details(
                        DCInfo("20230412", CPP, 11.0, "5DX56703VP7681735", "$")
                    )
                )
            )
            add(
                DInfo(
                    "Xeeling", details(
                        DCInfo("20230417-101105", CAP, 18.0, "2023041722001431191456925759")
                    )
                )
            )
            add(
                DInfo(
                    "花沫", details(
                        DCInfo("20230419-130018", CQQ, 10.0, "101000026901302304191391141788")
                    )
                )
            )
            add(
                DInfo(
                    "加油加油", details(
                        DCInfo("20230424-130412", CWC, 20.0, "10000499012023042401004542917525")
                    )
                )
            )
        }
    }

    private fun details(vararg info: DCInfo): Array<DCInfo> {
        return info.toMutableList().toTypedArray()
    }

    fun showDonateList() {
        MaterialAlertDialogBuilder(context, dialogCentered).apply {
            val data = ArrayList<DInfo>(getDonateData())
            val develop = context.getBoolean(SettingsPrefs, "hidden_function", false)
            if (develop) {
                var count = 0.0
                var chsCount = 0.0
                var otherCount = 0.0
                data.forEach { its ->
                    its.details.forEach {
                        count++
                        when (it.unit) {
                            "RMB" -> chsCount += it.money
                            "$" -> otherCount += it.money
                        }
                    }
                }
                data.add(
                    0,
                    DInfo(
                        "${DecimalFormat("0.00").format(chsCount)}RMB\n${
                            DecimalFormat("0.00").format(otherCount)
                        }$",
                        details(DCInfo("", CNU, count, "", ""))
                    )
                )
            }
            setTitle(context.getString(R.string.donation_list))
            setView(
                RecyclerView(context).apply {
                    setPadding(0, 10.dp, 0, 10.dp)
                    adapter = DonateListAdapter(context, data)
                    layoutManager = LinearLayoutManager(context)
                }
            )
        }.show()
    }

    fun showQRCode(base64: String) {
        val dialog = MaterialAlertDialogBuilder(
            context, dialogCentered
        ).apply {
            setTitle(context.getString(R.string.qq))
            setView(R.layout.layout_donate_dialog)
        }.show()
        dialog.findViewById<MaterialTextView>(R.id.donate_message)?.text =
            context.getString(R.string.donate_message)
        dialog.findViewById<ImageView>(R.id.donate_image)
            ?.setImageBitmap(base64ToBitmap(base64))
    }
}

@Suppress("ArrayInDataClass")
data class DInfo(
    val name: String,
    val details: Array<DCInfo>
) : Serializable

data class DCInfo(
    val time: String,
    val channel: String,
    val money: Double,
    val order: String,
    val unit: String = "RMB"
) : Serializable

class DonateListAdapter(val context: Context, val data: ArrayList<DInfo>) :
    RecyclerView.Adapter<DonateListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutDonateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        data[position].details.apply {
            var isChs = false
            var isOther = false
            var count = 0.0
            var chsCount = 0.0
            var otherCount = 0.0
            forEach {
                when (it.unit) {
                    "RMB" -> {
                        isChs = true
                        chsCount += it.money
                    }

                    "$" -> {
                        isOther = true
                        otherCount += it.money
                    }

                    else -> count += it.money
                }
            }
            val newline = if (isChs && isOther) "\n" else ""
            val final =
                if (isChs) "$chsCount RMB" else "" + newline + if (isOther) "$otherCount $" else "" + if (count != 0.0) "$count" else ""
            holder.money.text = final
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(binding: LayoutDonateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.donateName
        val money = binding.donateMoney
    }
}