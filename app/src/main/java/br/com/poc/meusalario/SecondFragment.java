package br.com.poc.meusalario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.NumberFormat;
import java.util.Locale;


public class SecondFragment extends Fragment {

    private TextView tvSalarioBrutoTotal;
    private TextView tvINSSTotal;
    private TextView tvIRRFTotal;
    private TextView tvOutrosDescontosTotal;
    private TextView tvSalarioLiquidoTotal;
    private TextView tvDescontosTotal;

    private double salarioBruto;
    private double salarioLiquidoTotal ;
    private double descontosTotal;
    private double descontos;
    private int dependentes;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState  ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recuperarComponentesXml(view);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preencheCamposDeDados();
    }

    private void recuperarComponentesXml(@NonNull View view) {

        tvSalarioBrutoTotal = view.findViewById(R.id.txtViewSalarioBrutoTotal);
        tvINSSTotal = view.findViewById(R.id.txtViewINSSTotal);
        tvIRRFTotal =  view.findViewById(R.id.txtViewIRRFTotal);
        tvOutrosDescontosTotal = view.findViewById(R.id.txtViewOutrosDescontosTotal);
        tvSalarioLiquidoTotal = view.findViewById(R.id.txtViewSalarioLiquidoTotal);
        tvDescontosTotal =  view.findViewById(R.id.txtViewDescontosTotal);

        view.findViewById(R.id.btnVoltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }

    private void preencheCamposDeDados() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            salarioBruto = bundle.getDouble("salario");
            dependentes = bundle.getInt("dependentes");
            descontos = bundle.getDouble("descontos");


            calculaSalarioLiquido();
        }
    }

    private void calculaSalarioLiquido() {
        double descontosINSS = descontoINSS(salarioBruto);
        double descontoIRRF = descontoIRRF(salarioBruto,descontosINSS,dependentes);
        double percentualDesconto;
        descontosTotal = descontos + descontosINSS + descontoIRRF;
        salarioLiquidoTotal = salarioBruto - descontosTotal;

        percentualDesconto = descontosTotal/salarioBruto;

        mostraValores(descontosINSS, descontoIRRF,percentualDesconto);
    }

    private void mostraValores(double descontosINSS, double descontoIRRF, double vlrPercentual) {

        Locale localeBR = new Locale("pt","BR");
        NumberFormat percentual = NumberFormat.getPercentInstance(localeBR);
        percentual.setMinimumFractionDigits (2);

        tvSalarioBrutoTotal.setText(String.format("%.2f", salarioBruto));
        tvIRRFTotal.setText(String.format("-%.2f",descontoIRRF));
        tvINSSTotal.setText(String.format("-%.2f",descontosINSS));
        tvOutrosDescontosTotal.setText(String.format("-%.2f",descontos));
        tvSalarioLiquidoTotal.setText(String.format("%.2f",salarioLiquidoTotal));
        //tvDescontosTotal.setText(String.format("%.2f",vlrPercentual*100.0));
        tvDescontosTotal.setText(percentual.format(vlrPercentual));

    }

    public double descontoIRRF(double sal, double contribuicaoINSS,double nrDep){

            double descontoPorDependente = 189.59;
            double baseCalculo = sal - contribuicaoINSS - (nrDep * descontoPorDependente);

            Log.i("MeuSalario","baseCalculo=" + baseCalculo);

            double salarioFaixaUm = 1903.98;
            double salarioFaixaDois = 2826.65;
            double salarioFaixaTres = 3751.05;
            double salarioFaixaQuatro = 4664.68;
            double desconto = 0.0;
            double aliquota = 0.0;
            double deducao = 0.0;

            if (baseCalculo <= salarioFaixaUm){
                aliquota = 0.0;
                deducao = 0.0;
            }
            if (baseCalculo > salarioFaixaUm && baseCalculo <= salarioFaixaDois){
                aliquota = 0.075;
                deducao = 142.80;
            }
            if (baseCalculo > salarioFaixaDois && baseCalculo <= salarioFaixaTres){
                aliquota = 0.15;
                deducao = 354.80;
            }
            if (baseCalculo > salarioFaixaTres && baseCalculo <= salarioFaixaQuatro){
                aliquota = 0.225;
                deducao = 636.13;
            }
            if (baseCalculo > salarioFaixaQuatro ){
                aliquota = 0.275;
                deducao = 869.36;
            }

            desconto = (baseCalculo * aliquota) - deducao;

            return desconto;
    }

    public double descontoINSS(double sal){
        double salario = sal;
        double salarioFaixaUm = 1045.00;
        double salarioFaixaDois = 2089.60;
        double salarioFaixaTres = 3134.40;
        double salarioFaixaQuatro = 6101.06;
        double desconto = 0.0;
        double aliquota = 0.0;
        double deducao = 0.0;

        if (salario <= salarioFaixaUm){
            aliquota = 0.075;
            deducao = 0.0;
        }
        if (salario > salarioFaixaUm && salario <= salarioFaixaDois){
            aliquota = 0.09;
            deducao = 15.67;
        }
        if (salario > salarioFaixaDois && salario <= salarioFaixaTres){
            aliquota = 0.12;
            deducao = 78.36;
        }
        if (salario > salarioFaixaTres && salario <= salarioFaixaQuatro){
            aliquota = 0.14;
            deducao = 141.05;
        }
        if (salario > salarioFaixaQuatro ){
            desconto = 713.10; // desconto maximo é 713.10
        }

        if (desconto == 0.0){
            desconto = (salario * aliquota) - deducao;
        }

        return desconto;
    }


}