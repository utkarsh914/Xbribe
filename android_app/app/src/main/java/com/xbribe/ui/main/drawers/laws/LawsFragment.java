package com.xbribe.ui.main.drawers.laws;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LawsFragment extends Fragment

{


    @BindView(R.id.recycler_laws)
    RecyclerView recyclerView;

    LawsAdapter lawsAdapter;

    List<LawsModel> llist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent= inflater.inflate(R.layout.fragment_bribelaws,container,false);
        ButterKnife.bind(this,parent);
        initrecycleradapter();
        return parent;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            godisplay(position);
        }
    };

    private void godisplay(int position)
    {
        showMessage("AS STATED",llist.get(position).getDesc());
    }

    private void initrecycleradapter()
    {
        lawsAdapter=new LawsAdapter(getContext(),uploadlist());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(lawsAdapter);
        lawsAdapter.setOnItemClickListener(onClickListener);
    }

    private List<LawsModel> uploadlist()
    {
        llist=new ArrayList<>();
        llist.add(new LawsModel("SECTION 403-INDIAN PENAL CODE","who ever dishonestly misappropriates or converts to his own use any movable property,shall be punished with imprisonment of either description for a term which may extend to two years,or with fine,or with both."));
        llist.add(new LawsModel("SECTION 405-INDIAN PENAL CODE","Whoever,being in any manner entrusted with property,or with any dominion overproperty,dishonestly misappropriates or converts to his own use that property,or dishonestly uses or disposes of that property inviolation of any direction of law prescribing the mode in which such trust is to be discharged,or of any legal contract,expressor implied,which he has made touching the discharge of such trust,or willfully suffers any other person so to do,commits criminal breach of trust."));
        llist.add(new LawsModel("SECTION 406-INDIAN PENAL CODE","Punishment for criminal breach of trust Whoever commits criminal breach of trust shall be punished with imprisonment of either description for a term which may extend to three years,or with fine,or with both."));
        llist.add(new LawsModel("SECTION-408-INDIAN PENAL CODE","Whoever,being a clerk or servant or employ edasa clerk or servant,and being in any manner entrusted in such capacity with property,or with any dominion over property,commits criminal breach of trust in respect of that property,shall be punished with imprisonment of either description for a term which may extend to seven years,and shall also be liable to fine.")) ;
        llist.add(new LawsModel("SECTION 409-INDIAN PENAL CODE","Whoever,being in any manner entrusted with property,or with any dominion over property in his capacity of a public servant or in the way of his business as a banker,merchant,factor,broker,at torney or agent,commits breach of trust in respect of that property,shall be punished with imprisonment for life,or with imprisonment of either description for a term which may extend to ten years,and shall also be liable to fine."));
        llist.add(new LawsModel("SECTION 412-INDIAN PENAL CODE","Whoever dishonestly receives or retains any stolen property, the possession whereof he knows or has reason to believe to have been transferred by the commission of dacoity, or dishonestly receives from a person, whom he knows or has reason to believe to belong or to have belonged to a gang of dacoits, property which he knows or has reason to believe to have been stolen, shall be punished with 1 [imprisonment for life], or with rigorous imprisonment for a term which may extend to ten years, and shall also be liable to fine."));
        llist.add(new LawsModel("SECTION 415-INDIAN PENAL CODE","Whoever,by deceiving any person,fraudulently or dishonestly induces the person so deceived to deliver any property to any person,or to consent that any person shall retain any property,or intentionally induces the person so deceived to door omit to  do any thing which he would not do omit if he were not so deceived,and which act or omission causes or is likely to cause damage or harm to that person in body,mind,reputation or property,is said to cheat."));
        llist.add(new LawsModel("SECTION 417-INDIAN PPENAL CODE","Whoever cheats shall be punished with imprisonment of either description for a term which may extend to one year, or with fine, or with both."));
        llist.add(new LawsModel("SECTION 278-PROSECUTION SECTION OF INCOME TAX ACT,1961","Abetting or inducing another person to make and deliver an account or statement or declaration relating to any taxable income which is false and which he either knows or believes to be false."));
        llist.add(new LawsModel("SECTION 276-A-PROSECUTION SECTION OF INCOME TAX ACT,1961","Failure on the part of a liquidator or receiver of a company to give notice of his appointment to the Assessing Officer or failure to set apart amount notified by the Assessing Officer, or parting away of company’s properties in contravention of income-tax provision."));
        llist.add(new LawsModel("SECTION 271-A-PROSECUTION SECTION OF INCOME","Distribution of profits by registered firm otherwise than in accordance with partnership deed and as a result of which partner has returned income below the real income.Not exceeding 150 per cent of difference between tax on partner’s income assessed and tax on income returned, in addition to tax payable."));
        llist.add(new LawsModel("SECTION 3-APPOINTMENT OF JUDGES-PREVENTION OF CORRUPTION ACT-1988","Power To Appoint Special Judges: The Central and the State Government is empowered to appoint Special Judges by placing a Notification in the Official Gazette, to try the following offences: · Any offence punishable under this Act. · Any conspiracy to commit or any attempt to commit or any abetment of any of the offences specified under the Act. "));
        llist.add(new LawsModel("SECTION 4-CASE TRIABLE BY SPECIAL JUDGES -PREVENTION OF CORRUPTION ACT-1988","The offences punishable under this act can be tried by special Judges only. When trying any case, the special Judge is empowered to try any offence other than an offence punishable under this act, with which the accused may be charged at the same trial. It is recommended that the special Judge should hold the trial daily."));
        llist.add(new LawsModel("SECTION 5 -PROCEDURE AND POWER OF SPECIAL JUDGE-PREVENTION OF CORRUPTION ACT-1988","The following are the powers of the Special Judge: He may take cognizance of the offences without the accused being commissioned to him for trial. In trying the accused persons, shall follow the procedure prescribed by the Cr.P.C. for the trial of warrant cases by Magistrate. he may with a view to obtain the evidence of any person supposed to have been directly or indirectly concerned in or privy to an offence, tender pardon to such person provided that he would make full and true disclosure of the whole circumstances within his knowledge or in respect to any person related to the offence. "));
        llist.add(new LawsModel("AMENDMENT ACT- 2018-Definition of Undue Advantage-PREVENTION OF CORRUPTION ACT-1988","The terms “gratification other than legal remuneration” and “valuable thing” are being replaced by the term “undue advantage”. The bill, therefore, redefines the offense of accepting bribes as “obtains or accepts or attempts to obtain from any person an undue advantage, intending that in consequence a public duty would be performed improperly or dishonestly, either by himself or by another public servant is guilty of offense under section 7 and shall be imprisoned for a term of 3 to 7 years."));
        llist.add(new LawsModel("AMENDMENT ACT-Offering of Bribes by Person/Commercial Organization-PREVENTION OF CORRUPTION ACT-1988","n addition to treating bribe-giving as an offense, section 9 specifically provides for an offense by a commercial organization if any person associated with the commercial organization gives or promises to give any undue advantage to a public servant to obtain or retain business or an advantage in conduct of business."));
        llist.add(new LawsModel("AMENDMENT ACT -Redefining Criminal Misconduct-PREVENTION OF CORRUPTION ACT-1988","he offense of criminal misconduct specified in section 13 of the Prevention of Corruption Act, is being substituted by a new section restricting the criminal misconduct to dishonest or fraudulent misappropriation of any property entrusted to the public servant or if the public servant intentionally enriches himself illicitly during the period of his office."));
        llist.add(new LawsModel("AMENDMENT ACT-Prior Sanction of Appropriate Government for Investigation and Prosecution-PREVENTION OF CORRUPTION ACT-1988","he amendment extends the protection of requirement of prior approval to investigation prior to prosecution. Under the new Section 17A, except when a public official is caught ‘redhanded', the police cannot begin a probe, without the approval of the relevant authority, of any public official. Earlier, this was limited to protecting joint secretaries and above. This universal inclusion provides a great deal of protection to honest officials irrespective of their ranks or levels."));
        llist.add(new LawsModel("AMENDMENT ACT-Forfeiture of Property-PREVENTION OF CORRUPTION ACT-1988","The new Section 18A also introduces a provision for special courts to confiscate and attach the property acquired through corrupt practices."));
        llist.add(new LawsModel("AMENDMENT ACT - Time Frame for Trial-PREVENTION OF CORRUPTION ACT-1988","To ensure speedy justice, the Amendment Act now prescribes that the courts shall endeavor to complete the trial within 2 (two) years. This period can be extended by 6 (six) months at a time and up to a maximum of 4 (four) years in aggregate subject to proper reasons for the same being recorded."));
        llist.add(new LawsModel("Benami Transactions (Prohibition) Act, 1988","The act defines a 'benami' transaction as any transaction in which property is transferred to one person for consideration paid by another person. Such transactions were a feature of the Indian economy, usually relating to the purchase of property (real estate), and were thought to contribute to the Indian black money problem. The act bans all benami transactions and gives the government the right to recover property held benami without paying any compensation."));
        llist.add(new LawsModel("Black Money (Undisclosed Foreign Income and Assets) and Imposition of Tax Act, 2015","The goal of this law is to bring back the income and assets held abroad back to the country. As a result, only an Indian resident gets the opportunity to declare undisclosed assets .The government gives a time frame when someone can disclose assets. If the resident holding undisclosed assets declare the assets in the given time frame they are not subject to prosecution."));
        llist.add(new LawsModel("Prevention of Money Laundering Act, 2002","The Act and Rules notified there under impose obligation on banking companies, financial institutions and intermediaries to verify identity of clients, maintain records and furnish information in prescribed form to Financial Intelligence Unit - India (FIU-IND)"));

       return  llist;

    }
    private void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
