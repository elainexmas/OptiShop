package products;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.HashBasedTable;

public class ProductCollection {
	// ideally this will be a database table in the future
	// product I | store A's price | store B's price | etc...
	// product II | store A's price | store B's price | etc... 

	// for now, assume uniform quantities, thus each product has its own row (product I - 10 oz, product I - 20 oz)

	HashBasedTable<String, String, Double> productCollection;

	String[] stores;

	public ProductCollection(String filename) {
		productCollection = HashBasedTable.create();
		File f = new File(filename);

		BufferedReader read;

		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			CSVReader csvr = new CSVReader(read);
			List<String[]> fileEntries;

			try {
				fileEntries = csvr.readAll();

				/**
				 * [0] product [1] store A's price [2] store B's price [3] store C's price
				 */
				for (String[] strArray: fileEntries) {
					if (strArray[0].equals("")) {
						// first row
						stores = strArray;
						continue;
					} else {
						for (int i = 1; i < stores.length; i++) {
							productCollection.put(strArray[0], stores[i], Double.parseDouble(strArray[i].trim()));
						}
					}
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		String str = ",";
		for(String store : productCollection.columnKeySet()){
			str += store+",";
		}
		str += "\n";
		for(String product : productCollection.rowKeySet()){
			str += product+",";
			for(String store : productCollection.columnKeySet()){
				str += productCollection.get(product, store) +",";
			}
			str+="\n";
		}
		return (str);
	}

	public static void main(String[] args) {
		String filename = "/Users/Elaine/Documents/workspace/OptiShop/sample_prices.csv";
		ProductCollection pc = new ProductCollection(filename);

		System.out.println(pc.toString());
	}
}
