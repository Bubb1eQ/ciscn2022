<?php
/**
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @link             http://hizup.uk
 */

namespace TestProject\Controller;

class Admin extends Blog
{

    public function login()
    {
        if ($this->isLogged())
            header('Location: ' . ROOT_URL . '?p=blog&a=all');

        if (isset($_POST['email'], $_POST['password']))
        {
            $this->oUtil->getModel('Admin');
            $this->oModel = new \TestProject\Model\Admin;

            if ($this->oModel->login($_POST['email'], $_POST['password']))
            {
                $_SESSION['is_logged'] = 1; // Admin is logged now
                header('Location: ' . ROOT_URL . '?p=blog&a=all');
                exit;
            }
            else
                $this->oUtil->sErrMsg = 'Incorrect Login!';
        }
        $this->oUtil->getView('login');
    }

    public function logout()
    {
        if (!$this->isLogged()) exit;

        // If there is a session, destroy it to disconnect the admin
        if (!empty($_SESSION))
        {
            $_SESSION = array();
            session_unset();
            session_destroy();
        }

        // Redirect to the homepage
        header('Location: ' . ROOT_URL);
        exit;
    }
    public function adminupload()
    {
        $ip = $_SERVER['REMOTE_ADDR'];
        if($ip != '127.0.0.1'){
            exit();
        }
        if (!empty($_FILES)) {

            if ($_FILES["file"]["error"] > 0 || $_FILES["file"]["size"] > 20000)
            {
                die("Error, maybe the file size is too large.");
            }
            else
            {
                $arr = explode(".", $_FILES["file"]["name"]);
                echo "Upload: " . $_FILES["file"]["name"] . "<br />";
                echo "Type: " . $_FILES["file"]["type"] . "<br />";
                echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
                echo "Stored in: " . $_FILES["file"]["tmp_name"];
                if (file_exists(ROOT_PATH . 'Upload/' . $_FILES["file"]["name"]))
                {
                    echo $_FILES["file"]["name"] . " already exists. ";
                }
                else
                {
                    move_uploaded_file($_FILES["file"]["tmp_name"], ROOT_PATH . 'Upload/' . $_FILES["file"]["name"]);
                    echo "Stored in: " . ROOT_PATH . 'Upload/' . $_FILES["file"]["name"];
                    #system("convert " . ROOT_PATH . 'Upload/' . $_FILES["file"]["name"] . " " . ROOT_PATH . 'Upload/' . $_FILES["file"]["name"] . ".convert");
                }
            }
        }else{
            $this->oUtil->getView('upload');
        }
    }

}
