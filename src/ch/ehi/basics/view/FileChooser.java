package ch.ehi.basics.view;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import ch.ehi.basics.i18n.MessageFormat;

public class FileChooser extends JFileChooser {
  static java.util.ResourceBundle rsrc = ch.ehi.basics.i18n.ResourceBundle.getBundle(FileChooser.class);
  public FileChooser() {super();}
  public FileChooser(File currentDirectory) {super(currentDirectory);}
  public FileChooser(File currentDirectory, FileSystemView fsv) {super(currentDirectory,fsv);}
  public FileChooser(FileSystemView fsv) {super(fsv);}
  public FileChooser(String currentDirectoryPath) {super(currentDirectoryPath);}
  public FileChooser(String currentDirectoryPath, FileSystemView fsv) {super(currentDirectoryPath,fsv);}

   public void approveSelection() {
        	File selectedFile = getSelectedFile();
        	if (selectedFile == null){
                  return;
                }
          javax.swing.filechooser.FileFilter filter=getFileFilter();
          // is it our well known filter?
          if(filter instanceof GenericFileFilter){
            // file without extension selected?
            if(GenericFileFilter.getFileExtension(selectedFile)==null){
                  // add extension of current filter
                  selectedFile=new File(selectedFile.getAbsolutePath()+"."+((GenericFileFilter)filter).getExtension());
                  setSelectedFile(selectedFile);
            }
          }
        if(getDialogType()==JFileChooser.OPEN_DIALOG){
                if(!selectedFile.isFile()) {
      	          // Post an error message and return
        	   JOptionPane.showMessageDialog(this,
        		rsrc.getString("CTfiledoesntexist"),
        		getDialogTitle(),
        		JOptionPane.ERROR_MESSAGE);
      	            // Return - causes the dialog to stay posted
      		    return;
      	        }
        }else if(getDialogType()==JFileChooser.SAVE_DIALOG){
			// file already exists?
			if (selectedFile.exists()) {
				// selected file not writeable?
				if (!selectedFile.canWrite()) {
					// Post an error message and return
					JOptionPane.showMessageDialog(
						this,
						rsrc.getString("CTfileisntwriteable"),
						getDialogTitle(),
						JOptionPane.ERROR_MESSAGE);
					// Return - causes the dialog to stay posted
					return;
				}
				// let user approve to overwrite file
				// Post an option dialog
				int response =
					JOptionPane.showConfirmDialog(
						this,
						rsrc.getString("CTfileexists"),
						getDialogTitle(),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
	
				if (response == JOptionPane.CLOSED_OPTION
					|| response == JOptionPane.CANCEL_OPTION) {
					// Carry out the cancel action - drops the dialog
					cancelSelection();
					return;
				} else if (response == JOptionPane.YES_OPTION) {
					// ok, overwrite
				} else {
					// Return - causes the dialog to stay posted
					return;
				}
			}
        }else if(isOutputDirDialog){
			// file already exists?
			if (selectedFile.exists()) {
				// selected file not writeable?
				if (!selectedFile.canWrite()) {
					// Post an error message and return
					JOptionPane.showMessageDialog(
						this,
						rsrc.getString("CTdirisntwriteable"),
						getDialogTitle(),
						JOptionPane.ERROR_MESSAGE);
					// Return - causes the dialog to stay posted
					return;
				}
				// let user approve to overwrite file
				// Post an option dialog
				int response =
					JOptionPane.showConfirmDialog(
						this,
						MessageFormat.format(rsrc,"CTdirexists",selectedFile.getAbsolutePath()),
						getDialogTitle(),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
	
				if (response == JOptionPane.CLOSED_OPTION
					|| response == JOptionPane.CANCEL_OPTION) {
					// Carry out the cancel action - drops the dialog
					cancelSelection();
					return;
				} else if (response == JOptionPane.YES_OPTION) {
					// ok, overwrite
				} else {
					// Return - causes the dialog to stay posted
					return;
				}
			}
        }

	// let OK action continue
	super.approveSelection();
   }
   private boolean isOutputDirDialog=false;
   public int showOutputDirDialog(java.awt.Component parent)
   {
	setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	setFileFilter(new GenericFileFilter(rsrc.getString("CTdirectories"),""));
	setDialogTitle(rsrc.getString("CToutputDirTitle"));
	isOutputDirDialog=true;
	int ret=CANCEL_OPTION;
	try{
		ret=showDialog(parent,rsrc.getString("CTdirectoryApproveButton"));
	}finally{
		isOutputDirDialog=false;
	}
	return ret;
   }
   public int showInputDirDialog(java.awt.Component parent)
   {
	setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	setFileFilter(new GenericFileFilter(rsrc.getString("CTdirectories"),""));
	setDialogTitle(rsrc.getString("CTinputDirTitle"));
	return showDialog(parent,rsrc.getString("CTdirectoryApproveButton"));
   }
}

