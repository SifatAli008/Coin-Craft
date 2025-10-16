using System.Collections;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class UIFade : Singleton<UIFade>
{
    [SerializeField] private Image fadeScreen;
    [SerializeField] private float fadeSpeed = 1f;

    private IEnumerator fadeRoutine;
    public bool IsFading { get; private set; } = false;

    protected override void Awake()
    {
        base.Awake();

        if (fadeScreen != null)
        {
            // Start fully black for fade-in effect
            fadeScreen.color = new Color(0, 0, 0, 1);
        }
        else
        {
            Debug.LogError("Fade screen Image not assigned in UIFade.");
        }
    }

    private void Start()
    {
        // Automatically fade in at the start of any scene
        FadeToClear();
    }

    private void OnEnable()
    {
        SceneManager.sceneLoaded += OnSceneLoaded;
    }

    private void OnDisable()
    {
        SceneManager.sceneLoaded -= OnSceneLoaded;
    }

    private void OnSceneLoaded(Scene scene, LoadSceneMode mode)
    {
        // Fade in when scene is loaded
        FadeToClear();
    }

    public void FadeToBlack()
    {
        if (fadeRoutine != null)
            StopCoroutine(fadeRoutine);

        fadeRoutine = FadeRoutine(1f);
        StartCoroutine(fadeRoutine);
    }

    public void FadeToClear()
    {
        if (fadeRoutine != null)
            StopCoroutine(fadeRoutine);

        fadeRoutine = FadeRoutine(0f);
        StartCoroutine(fadeRoutine);
    }

    private IEnumerator FadeRoutine(float targetAlpha)
    {
        IsFading = true;

        while (!Mathf.Approximately(fadeScreen.color.a, targetAlpha))
        {
            float alpha = Mathf.MoveTowards(fadeScreen.color.a, targetAlpha, fadeSpeed * Time.deltaTime);
            fadeScreen.color = new Color(fadeScreen.color.r, fadeScreen.color.g, fadeScreen.color.b, alpha);
            yield return null;
        }

        IsFading = false;
    }
}
