<?php
/**
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @link             http://hizup.uk
 */

namespace TestProject\Controller;

class Blog
{

    protected $oUtil, $oModel;
    private $_iId;
    private $render;

    public function __construct()
    {
        // Enable PHP Session
        if (empty($_SESSION))
            @session_start();

        $this->oUtil = new \TestProject\Engine\Util;

        /** Get the Model class in all the controller class **/
        $this->oUtil->getModel('Blog');
        $this->oModel = new \TestProject\Model\Blog;
        $this->render = (int) (!empty($_GET['r']) ? $_GET['r'] : 0);
        /** Get the Post ID in the constructor in order to avoid the duplication of the same code **/
        $this->_iId = (int) (!empty($_GET['id']) ? $_GET['id'] : 0);
    }


    /***** Front end *****/
    // Homepage
    public function index()
    {
        $this->oUtil->oPosts = $this->oModel->get(0, 5); // Get only the latest 5 posts

        $this->oUtil->getView('index');

    }

    public function post()
    {
        if($this->render == 0){
            $this->oUtil->oPost = $this->oModel->getById($this->_iId); // Get the data of the post

            $this->oUtil->getView('post');
        }else{
            $this->oUtil->__get($_GET['f']);
        }

    }

    public function notFound()
    {
        $this->oUtil->getView('not_found');
    }


    /***** For Admin (Back end) *****/
    public function all()
    {
        if (!$this->isLogged()) exit;

        $this->oUtil->oPosts = $this->oModel->getAll();

        $this->oUtil->getView('index');
    }


    public function add()
    {
        if (!$this->isLogged()) exit;


        if (!empty($_POST['add_submit']))
        {
            if (isset($_POST['title'], $_POST['body']) && mb_strlen($_POST['title']) <= 50) // Allow a maximum of 50 characters
            {
                $aData = array('title' => $_POST['title'], 'body' => $_POST['body'], 'created_date' => date('Y-m-d H:i:s'));

                if ($this->oModel->add($aData))
                    $this->oUtil->sSuccMsg = 'Hurray!! The post has been added.';
                else
                    $this->oUtil->sErrMsg = 'Whoops! An error has occurred! Please try again later.';
            }
            else
            {
                $this->oUtil->sErrMsg = 'All fields are required and the title cannot exceed 50 characters.';
            }
        }


        $this->oUtil->getView('add_post');
    }

    public function edit()
    {
        if (!$this->isLogged()) exit;

        if (!empty($_POST['edit_submit']))
        {
            if (isset($_POST['title'], $_POST['body']))
            {
                $aData = array('post_id' => $this->_iId, 'title' => $_POST['title'], 'body' => $_POST['body']);

                if ($this->oModel->update($aData))
                    $this->oUtil->sSuccMsg = 'Hurray! The post has been updated.';
                else
                    $this->oUtil->sErrMsg = 'Whoops! An error has occurred! Please try again later';
            }
            else
            {
                $this->oUtil->sErrMsg = 'All fields are required.';
            }
        }

        /* Get the data of the post */
        $this->oUtil->oPost = $this->oModel->getById($this->_iId);

        $this->oUtil->getView('edit_post');
    }

    public function delete()
    {
        if (!$this->isLogged()) exit;

        if (!empty($_POST['delete']) && $this->oModel->delete($this->_iId))
            header('Location: ' . ROOT_URL);
        else
            exit('Whoops! Post cannot be deleted.');
    }

    public function upload()
    {
        if (!empty($_FILES)) {

            if ($_FILES["file"]["error"] > 0 || $_FILES["file"]["size"] > 20000)
            {
                die("Error, maybe the file size is too large.");
            }
            else
            {
                $arr = explode(".", $_FILES["file"]["name"]);
                if (in_array($arr[count($arr) - 1],["php","phtml","php5","php7"])) {
                    echo "Wrong ! You cant upload a php file.";
                }elseif ($arr[count($arr) - 1] == ""){
                    echo "Extension Error.";
                }else{
                    if(stripos(file_get_contents($_FILES["file"]["tmp_name"]),'<?') != NULL){
                        die('PHP Content Detected;');
                    }
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

                    }
                }
            }
        }else{
            $this->oUtil->getView('upload');
        }
    }

    protected function isLogged()
    {
        return !empty($_SESSION['is_logged']);
    }

}
