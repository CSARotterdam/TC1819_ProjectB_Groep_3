package groep3.hr.nl.techlabhr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> products;
    Context mContext;

    //View lookup cache
    private static class ViewHolder{
        ImageView itemIcon;
        TextView productID;
        TextView productName;
        TextView productStock;
    }

    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.product_list_item, products);
        this.products = products;
        this.mContext = context;

    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Product product = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_list_item, parent, false);
            viewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.productID = (TextView) convertView.findViewById(R.id.pid);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.productStock = (TextView) convertView.findViewById(R.id.product_stock);

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;
        viewHolder.itemIcon.setImageBitmap(product.getIcon());
        viewHolder.productID.setText(product.getId());
        viewHolder.productName.setText(product.getName());
        viewHolder.productStock.setText(product.getStock());

        return convertView;
    }
}
