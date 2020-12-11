package br.com.poc.meusalario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    private EditText edtViewSalarioBruto;
    private EditText edtViewDependentes;
    private EditText edtViewOutrosDescontos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recuperarComponentesXml(view);
    }

    private void recuperarComponentesXml(@NonNull View view) {

        edtViewSalarioBruto = view.findViewById(R.id.edtViewSalarioBruto);
        edtViewDependentes =  view.findViewById(R.id.edtViewDependentes);
        edtViewOutrosDescontos =  view.findViewById(R.id.edtViewOutrosDescontos);

        view.findViewById(R.id.btnCalcular).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaDados(view);

            }
        });

    }

    private void validaDados(View view) {
        String salario = edtViewSalarioBruto.getText().toString();
        String dependentes = edtViewDependentes.getText().toString();
        String outrosDescontos = edtViewOutrosDescontos.getText().toString();

        boolean b = false;
        if (salario != null){
            try {
                Double.parseDouble(salario);
                b = true;
            } catch (NumberFormatException e) {
                b = false;
            }
            if (!b){
                mostraMensagem("Valor inválido no campo salário!!!");
                return;
            }
        }else{
            mostraMensagem("Informe o Salário!!!");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putDouble("salario",Double.parseDouble(salario));
        bundle.putInt("dependentes", Integer.parseInt(dependentes));
        bundle.putDouble("outrosDescontos",Double.parseDouble(outrosDescontos));

        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
    }


    private void mostraMensagem(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Erro");
        alert.setMessage( msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}