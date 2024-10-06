import {
  AccessibilityHelp,
  Alignment,
  Autoformat,
  AutoImage,
  Autosave,
  BalloonToolbar,
  BlockQuote,
  BlockToolbar,
  Bold,
  ClassicEditor,
  Code,
  CodeBlock,
  Essentials,
  FindAndReplace,
  FontBackgroundColor,
  FontColor,
  FontFamily,
  FontSize,
  Heading,
  Highlight,
  HorizontalLine,
  ImageBlock,
  ImageCaption,
  ImageInline,
  ImageInsert,
  ImageInsertViaUrl,
  ImageResize,
  ImageStyle,
  ImageTextAlternative,
  ImageToolbar,
  ImageUpload,
  Indent,
  IndentBlock,
  Italic,
  Link,
  LinkImage,
  List,
  ListProperties,
  MediaEmbed,
  PageBreak,
  Paragraph,
  PasteFromOffice,
  SelectAll,
  SimpleUploadAdapter,
  SpecialCharacters,
  SpecialCharactersArrows,
  SpecialCharactersCurrency,
  SpecialCharactersEssentials,
  SpecialCharactersLatin,
  SpecialCharactersMathematical,
  SpecialCharactersText,
  Strikethrough,
  Subscript,
  Superscript,
  Table,
  TableCaption,
  TableCellProperties,
  TableColumnResize,
  TableProperties,
  TableToolbar,
  TextTransformation,
  Title,
  TodoList,
  Underline,
  Undo
} from 'ckeditor5';

import translations from 'ckeditor5/translations/ko.js';

// CkEditor 에서 주는 에디터 설정
const editorConfig = {
  toolbar: {
    items: [
      'undo',
      'redo',
      '|',
      'heading',
      '|',
      'fontSize',
      'fontFamily',
      'fontColor',
      'fontBackgroundColor',
      '|',
      'bold',
      'italic',
      'underline',
      '|',
      'link',
      'insertImage',
      'insertTable',
      'highlight',
      'blockQuote',
      'codeBlock',
      '|',
      'alignment',
      '|',
      'bulletedList',
      'numberedList',
      'todoList',
      'outdent',
      'indent'
    ],
    shouldNotGroupWhenFull: false
  },
  plugins: [
    AccessibilityHelp,
    Alignment,
    Autoformat,
    AutoImage,
    Autosave,
    BalloonToolbar,
    BlockQuote,
    BlockToolbar,
    Bold,
    Code,
    CodeBlock,
    Essentials,
    FindAndReplace,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    Heading,
    Highlight,
    HorizontalLine,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Indent,
    IndentBlock,
    Italic,
    Link,
    LinkImage,
    List,
    ListProperties,
    MediaEmbed,
    PageBreak,
    Paragraph,
    PasteFromOffice,
    SelectAll,
    SimpleUploadAdapter,
    SpecialCharacters,
    SpecialCharactersArrows,
    SpecialCharactersCurrency,
    SpecialCharactersEssentials,
    SpecialCharactersLatin,
    SpecialCharactersMathematical,
    SpecialCharactersText,
    Strikethrough,
    Subscript,
    Superscript,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextTransformation,
    Title,
    TodoList,
    Underline,
    Undo
  ],
  balloonToolbar: ['bold', 'italic', '|', 'link', 'insertImage', '|',
    'bulletedList', 'numberedList'],
  blockToolbar: [
    'fontSize',
    'fontColor',
    'fontBackgroundColor',
    '|',
    'bold',
    'italic',
    '|',
    'link',
    'insertImage',
    'insertTable',
    '|',
    'bulletedList',
    'numberedList',
    'outdent',
    'indent'
  ],
  fontFamily: {
    supportAllValues: true
  },
  fontSize: {
    options: [10, 12, 14, 'default', 18, 20, 22],
    supportAllValues: true
  },
  heading: {
    options: [
      {
        model: 'paragraph',
        title: 'Paragraph',
        class: 'ck-heading_paragraph'
      },
      {
        model: 'heading1',
        view: 'h1',
        title: 'Heading 1',
        class: 'ck-heading_heading1'
      },
      {
        model: 'heading2',
        view: 'h2',
        title: 'Heading 2',
        class: 'ck-heading_heading2'
      },
      {
        model: 'heading3',
        view: 'h3',
        title: 'Heading 3',
        class: 'ck-heading_heading3'
      },
      {
        model: 'heading4',
        view: 'h4',
        title: 'Heading 4',
        class: 'ck-heading_heading4'
      },
      {
        model: 'heading5',
        view: 'h5',
        title: 'Heading 5',
        class: 'ck-heading_heading5'
      },
      {
        model: 'heading6',
        view: 'h6',
        title: 'Heading 6',
        class: 'ck-heading_heading6'
      }
    ]
  },
  image: {
    toolbar: [
      'toggleImageCaption',
      'imageTextAlternative',
      '|',
      'imageStyle:inline',
      'imageStyle:wrapText',
      'imageStyle:breakText',
      '|',
      'resizeImage'
    ]
  },
  language: 'ko',
  link: {
    addTargetToExternalLinks: true,
    defaultProtocol: 'https://',
    decorators: {
      toggleDownloadable: {
        mode: 'manual',
        label: 'Downloadable',
        attributes: {
          download: 'file'
        }
      }
    }
  },
  list: {
    properties: {
      styles: true,
      startIndex: true,
      reversed: true
    }
  },
  menuBar: {
    isVisible: true
  },
  table: {
    contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells',
      'tableProperties', 'tableCellProperties']
  },
  translations: [translations]
};

// 이미지 관련 처리 위해서 editorConfig 에 몇몇 속성 추가함

// CKEditor 의 `Simple Upload Adapter` 자체 플러그인을 이용
// 이는 이미지를 `XMLHttpRequest` API 를 이용해 우리 서버에 저장할 수 있게 해준다고 함.
// `XMLHttpRequest` 이 뭔지는 모름.
// 여튼 이걸 이용해 이미지를 GCS 에 저장하고, 저장된 이미지 URL 을 받아서 CKEditor 에게 뱉어줄 수 있음.
// 그럼 CKEditor 는 그 URL 을 HTML 이미지 태그 src 에 잘 바꿔줌.
// https://ckeditor.com/docs/ckeditor5/latest/features/images/image-upload/simple-upload-adapter.html#server-side-configuration
editorConfig.simpleUpload = {

  // The URL that the images are uploaded to.
  // editor 에서 이미지 넣으면 우리한테 이미지 전달할 API (POST)
  uploadUrl: '/image/upload',

  // Enable the XMLHttpRequest.withCredentials property.
  // ?? 이건 뭔지 모르겠음. 없어도 그냥 잘 되는데 뭐지??
  // 이거 설정하면 cross-site request 요청을 위해 추가적인 HTTP 헤더 설정이 필요하다는데 뭔지 몰??루???
  withCredentials: false,

  // Headers sent along with the XMLHttpRequest to the upload server.
  // TODO 나중에 CKEditor 에서도 JWT 토큰 포함해서 요청 보내야 될 수도 있는데 어떠카지??
  // TODO 만약 `Authorization` 에 토큰 넣어서 repo 에 올리면 취약점 공개하는거 아님?? ㄹㅇ 어캄???
  // headers: {
  //   'X-CSRF-TOKEN': 'CSRF-Token',
  //   Authorization: 'Bearer <JSON Web Token>'
  // }

  /**
   * !!!!!!!!!!!!!!!!! 주의 !!!!!!!!!!!!!!!!!
   * ---------------------------------------
   *
   * CKEditor 문서에 따르면 upload 가 성공적으로 이루어졌을 때, 아래와 같은 형식을 지켜 응답이 와야 한다고 함.
   *
   * {
   *   "url": "https://example.com/images/foo.jpg"
   * }
   *
   * 나중에 까먹지 말자
   *
   * https://ckeditor.com/docs/ckeditor5/latest/features/images/image-upload/simple-upload-adapter.html#successful-upload
   * ---------------------------------------
   */

}

// CkEditor - Classic 버전으로 생성
ClassicEditor.create(document.querySelector('#editor'), editorConfig)
.then(editor => {
  // `post-form` id 가진 form 태그에서 submit 시 본문 내용 넣어주는 listener 추가
  document.getElementById('post-form').addEventListener('submit',
      function (event) {

        // 그냥 default 설정된 submit 하지 않도록 설정
        event.preventDefault();

        // 에디터 본문 내용 `hidden-content` 의 값으로 집어넣기
        document.getElementById('hidden-content').value = editor.getData(); // Get editor content

        // submit
        this.submit();
      });
});