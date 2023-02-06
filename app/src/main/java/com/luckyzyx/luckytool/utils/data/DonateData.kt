package com.luckyzyx.luckytool.utils.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.databinding.LayoutDonateItemBinding
import java.io.Serializable

@Suppress("PrivatePropertyName")
@Obfuscate
class DonateData {
    private val CQQ = "QQ"
    private val CQQHB = "QQHB"
    private val CWC = "WeChat"
    private val CAP = "AliPay"
    fun getData(): ArrayList<DonateInfo> {
        return ArrayList<DonateInfo>().apply {
            add(
                DonateInfo("是小奶糖啊", 33.0, donateDetails(
                    DonateChannelInfo("20220912-003911", CWC, 1.0, "10000499012022091201251965341571"),
                    DonateChannelInfo("20220912-003928", CWC, 1.0, "10001071012022091201925356060626"),
                    DonateChannelInfo("20220912-003952", CWC, 1.0, "10000499012022091201394232947529"),
                    DonateChannelInfo("20220912-004010", CWC, 1.0, "10000499012022091201117667651586"),
                    DonateChannelInfo("20220912-004034", CWC, 1.0, "10001071012022091201573323684619"),
                    DonateChannelInfo("20220912-004102", CWC, 1.0, "10000499012022091201061912891540"),
                    DonateChannelInfo("20220912-004120", CWC, 1.0, "10000499012022091201695371491603"),
                    DonateChannelInfo("20220912-004138", CWC, 1.0, "10000499012022091201029478590602"),
                    DonateChannelInfo("20220912-004158", CWC, 1.0, "10000499012022091201421833786544"),
                    DonateChannelInfo("20220912-004216", CWC, 1.0, "10000499012022091201781561301536"),
                    DonateChannelInfo("20220912-230934", CWC, 1.0, "10000499012022091201534268361646"),
                    DonateChannelInfo("20220912-231016", CWC, 1.0, "10001071012022091201531626564574"),
                    DonateChannelInfo("20220912-231043", CWC, 1.0, "10001071012022091201628715912641"),
                    DonateChannelInfo("20220912-231103", CWC, 1.0, "10000499012022091201917012382639"),
                    DonateChannelInfo("20220912-231130", CWC, 1.0, "10001071012022091201327282039502"),
                    DonateChannelInfo("20220915-005135", CAP, 0.1, "2022091522001418391439574777"),
                    DonateChannelInfo("20220915-005314", CAP, 0.2, "2022091522001418391438389614"),
                    DonateChannelInfo("20220915-005343", CAP, 0.3, "2022091522001418391439574778"),
                    DonateChannelInfo("20220915-005408", CAP, 0.4, "2022091522001418391438987850"),
                    DonateChannelInfo("20220915-005915", CAP, 5.0, "2022091522001418391438398792"),
                    DonateChannelInfo("20230125-145957", CQQ, 11.0, "101000026901302301251417959764"),
                )
                )
            )
            add(DonateInfo("Kimjaejiang", 5.0, donateDetails(
                DonateChannelInfo("20220912-205238",CWC,5.0,"10000499012022091200755856699085")
            )))
            add(DonateInfo("*耿",1.0, donateDetails(
                DonateChannelInfo("20220914-083334",CWC,1.0,"10001071012022091401684644133513")
            )))
            add(DonateInfo("邹王", 66.66, donateDetails(
                DonateChannelInfo("20220916-233422",CQQ,15.0,"101000026901502209161435139989"),
                DonateChannelInfo("20221110-103510",CQQHB,51.66,"1000032001221110360010904620380000")
            )))
            add(DonateInfo("*す", 20.0, donateDetails(
                DonateChannelInfo("20220919-235632",CWC,20.0,"10001071012022091900284575580158")
            )))
            add(DonateInfo("楠", 10.0, donateDetails(
                DonateChannelInfo("20220925-204804",CAP,10.0,"2022092522001495601447752596")
            )))
            add(DonateInfo("天伞桜", 88.8, donateDetails(
                DonateChannelInfo("20220926-083025",CAP,88.8,"2022092622001422141429313950")
            )))
            add(DonateInfo("*伟",15.0, donateDetails(
                DonateChannelInfo("20220930-232126",CAP,15.0,"2022093022001491561432064831")
            )))
            add(DonateInfo("ssd·风格", 5.0, donateDetails(
                DonateChannelInfo("20221012-203606",CAP,5.0,"2022101222001475911409839972")
            )))
            add(DonateInfo("**智", 15.0, donateDetails(
                DonateChannelInfo("20221013-072635",CAP,15.0,"2022101322001464631446819251")
            )))
            add(DonateInfo("*恩", 10.0, donateDetails(
                DonateChannelInfo("20221013-113220",CAP,10.0,"2022101322001401321434146632")
            )))
            add(DonateInfo("佘樂", 20.0, donateDetails(
                DonateChannelInfo("20221017-090951",CWC,20.0,"10001071012022101700116472524081")
            )))
            add(DonateInfo("风冷涂的蜡", 10.0, donateDetails(
                DonateChannelInfo("20221018-131217",CQQ,10.0,"101000026901502210181449517794")
            )))
            add(DonateInfo("**才", 10.0, donateDetails(
                DonateChannelInfo("20221018-122213",CAP,10.0,"2022101822001424721421267656")
            )))
            add(DonateInfo("*灯", 5.0, donateDetails(
                DonateChannelInfo("20221019-195159",CWC,5.0,"10000499012022101901514527492524")
            )))
            add(DonateInfo("*G", 66.0, donateDetails(
                DonateChannelInfo("20221023-083920",CWC,66.0,"10001071012022102300250132780051")
            )))
            add(DonateInfo("*荣", 8.8, donateDetails(
                DonateChannelInfo("20221023-082337",CAP,8.8,"2022102322001418341416028048")
            )))
            add(DonateInfo("斯已", 15.0, donateDetails(
                DonateChannelInfo("20221025-232101",CAP,15.0,"2022102522001476341430603222")
            )))
            add(DonateInfo("冰梦", 20.0, donateDetails(
                DonateChannelInfo("20221028-122652",CAP,20.0,"2022102822001449721407673099")
            )))
            add(DonateInfo("*?", 8.80, donateDetails(
                DonateChannelInfo("20221029-204415",CWC,8.8,"10001071012022102900604818941128")
            )))
            add(DonateInfo("晨钟酱", 100.0, donateDetails(
                DonateChannelInfo("20221102-082807",CWC,100.0,"10000499012022110200766818668071")
            )))
            add(DonateInfo("小李.", 15.0, donateDetails(
                DonateChannelInfo("20221103",CAP,15.0,"2022110322001471241434100859")
            )))
            add(DonateInfo("yomol.", 18.0, donateDetails(
                DonateChannelInfo("20221109-174640",CWC,18.0,"10001071012022110901232470547613")
            )))
            add(DonateInfo("miComet35P", 10.0, donateDetails(
                DonateChannelInfo("20221110-153627",CAP,10.0,"2022111022001402571439794753")
            )))
            add(DonateInfo("347aidan", 5.0, donateDetails(
                DonateChannelInfo("20221111-082228",CWC,5.0,"10000499012022111101636288086525")
            )))
            add(DonateInfo("柳丝", 10.0, donateDetails(
                DonateChannelInfo("20221111-162944",CQQ,10.0,"101000026901302211111384241371")
            )))
            add(DonateInfo("*鱼", 5.0, donateDetails(
                DonateChannelInfo("20221115-123753",CWC,5.0,"10001071012022111501267896555529")
            )))
            add(DonateInfo("佘小鱼", 21.1, donateDetails(
                DonateChannelInfo("20221115-124129",CAP,21.1,"2022111522001494811407014490"),
                DonateChannelInfo("20230121-100513",CAP,10.0,"2023012122001494811424220936")
            )))
            add(DonateInfo("**程", 20.0, donateDetails(
                DonateChannelInfo("20221119-134103",CAP,20.0,"2022111922001416771433945337")
            )))
            add(DonateInfo("miro", 3.0, donateDetails(
                DonateChannelInfo("20221122-232621",CQQ,3.0,"101000026901302211221386081290")
            )))
            add(DonateInfo("你会振刀么", 18.88, donateDetails(
                DonateChannelInfo("20221123-005251",CWC,18.88,"10001071012022112301342736297502")
            )))
            add(DonateInfo("大挪移", 20.0, donateDetails(
                DonateChannelInfo("20221123-203918",CWC,20.0,"10000499012022112300516761031117")
            )))
            add(DonateInfo("**林", 10.0, donateDetails(
                DonateChannelInfo("20221125-093459",CAP,10.0,"2022112522001476991437614992")
            )))
            add(DonateInfo("*林", 10.0, donateDetails(
                DonateChannelInfo("20221128-030319",CWC,10.0,"10000499012022112801909258499610")
            )))
            add(DonateInfo("*₂", 6.0, donateDetails(
                DonateChannelInfo("20221130-231036",CWC,6.0,"10000499012022113001824358393615")
            )))
            add(DonateInfo("无", 5.0, donateDetails(
                DonateChannelInfo("20221202-162017",CQQ,5.0,"101000026901302212021417779297")
            )))
            add(DonateInfo("Mr.x", 20.0, donateDetails(
                DonateChannelInfo("20221204-192602",CWC,20.0,"10000499012022120400706169995060")
            )))
            add(DonateInfo("小妤", 13.14, donateDetails(
                DonateChannelInfo("20221202-034008",CAP,13.14,"2022120222001496361401339630")
            )))
            add(DonateInfo("*方", 5.0, donateDetails(
                DonateChannelInfo("20221204-230824",CWC,5.0,"10001071012022120401935056538617")
            )))
            add(DonateInfo("渐渐简单", 5.0, donateDetails(
                DonateChannelInfo("20221205-054441",CWC,5.0,"10001071012022120500559363563095")
            )))
            add(DonateInfo("㗽翳鮦峃", 6.66, donateDetails(
                DonateChannelInfo("20221210-163315",CAP,6.66,"2022121022001419861424041467")
            )))
            add(DonateInfo("Steven", 20.0, donateDetails(
                DonateChannelInfo("20221210-235500",CAP,20.0,"2022121022001456961433458318")
            )))
            add(DonateInfo("缺德导航", 15.0, donateDetails(
                DonateChannelInfo("20221212-144051",CAP,15.0,"2022121222001415271423722807")
            )))
            add(DonateInfo("萝卜啊", 10.0, donateDetails(
                DonateChannelInfo("20221219-143133",CWC,10.0,"10000499012022121901553619615591")
            )))
            add(DonateInfo("琉星", 6.66, donateDetails(
                DonateChannelInfo("20221222-184315",CWC,6.66,"10001071012022122201044935883530")
            )))
            add(DonateInfo("鸿鹄1207", 3.0, donateDetails(
                DonateChannelInfo("20221226-200021",CWC,3.0,"10001071012022122601752872360502")
            )))
            add(DonateInfo("ddddk", 5.0, donateDetails(
                DonateChannelInfo("20221229-225345",CWC,5.0,"10001071012022122901900634415576")
            )))
            add(DonateInfo("He_zhCN", 5.0, donateDetails(
                DonateChannelInfo("20230110-220546",CAP,5.0,"2023011022001440131458365074")
            )))
            add(DonateInfo("panfung", 18.0, donateDetails(
                DonateChannelInfo("20230111-181655",CAP,18.0,"2023011122001412191410136732")
            )))
            add(DonateInfo("G.Sheng", 25.0, donateDetails(
                DonateChannelInfo("20230117-120623",CWC,25.0,"10001071012023011700412830034162")
            )))
            add(DonateInfo("故里", 5.0, donateDetails(
                DonateChannelInfo("20230119-002610",CQQHB,5.0,"1000046601230119390010781395540000")
            )))
            add(DonateInfo("散乱的钟", 6.8, donateDetails(
                DonateChannelInfo("20230119-152310",CWC,6.8,"10001071012023011901412388984600")
            )))
            add(DonateInfo("项欣", 1.0, donateDetails(
                DonateChannelInfo("20230121-170458",CWC,1.0,"10000499012023012101792786463501")
            )))
            add(DonateInfo("我是大笨熊",20.0, donateDetails(
                DonateChannelInfo("20230125-061251",CQQ,20.0,"101000026901502301251436803564")
            )))
            add(DonateInfo("鱼知乐",20.0, donateDetails(
                DonateChannelInfo("20230125-094400",CAP,20.0,"2023012522001424271419420653")
            )))
            add(DonateInfo("高木同学咩",8.8, donateDetails(
                DonateChannelInfo("20230126-000631",CWC,8.8,"10001071012023012600559933304093")
            )))
            add(DonateInfo("是麦芽糖呀",13.14, donateDetails(
                DonateChannelInfo("20230126-005542",CWC,13.14,"10001071012023012600871793553057")
            )))
            add(DonateInfo("贱贱",8.88, donateDetails(
                DonateChannelInfo("20230126-124451",CWC,8.88,"10000499012023012600637975717166")
            )))
            add(DonateInfo("一杯美式",1.3, donateDetails(
                DonateChannelInfo("20230129-092919",CWC,1.3,"10001071012023012900425726231103")
            )))
            add(DonateInfo("加两个卤蛋",5.0,donateDetails(
                DonateChannelInfo("20230205-032712",CWC,5.0,"10000499012023020501552678244628")
            )))
            add(DonateInfo("Zhang_ZL",10.0,donateDetails(
                DonateChannelInfo("20230206-134646",CWC,10.0,"10000499012023020601408947796566")
            )))
        }
    }

    private fun donateDetails(vararg info: DonateChannelInfo): Array<DonateChannelInfo> {
        return info.toMutableList().toTypedArray()
    }
}

@Suppress("ArrayInDataClass")
data class DonateInfo(
    val name: String,
    val money: Double,
    val details: Array<DonateChannelInfo>
) : Serializable

data class DonateChannelInfo(
    val time: String,
    val channel: String,
    val money: Double,
    val order: String
) : Serializable

class DonateListAdapter(val context: Context, val data: ArrayList<DonateInfo>) :
    RecyclerView.Adapter<DonateListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutDonateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        data[position].details.apply {
            var count = 0.0
            forEach { count += it.money }
            holder.money.text = count.toString()
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