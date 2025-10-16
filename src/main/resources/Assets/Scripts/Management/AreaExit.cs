using System.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;

public class AreaExit : MonoBehaviour
{
    [SerializeField] private string sceneToLoad;
    [SerializeField] private string sceneTransitionName;

    private void OnTriggerEnter2D(Collider2D other)
    {
        if (other.gameObject.GetComponent<PlayerController>())
        {
            SceneManagement.Instance.SetTransitionName(sceneTransitionName);
            StartCoroutine(TransitionAndLoadScene());
        }
    }

    private IEnumerator TransitionAndLoadScene()
    {
        UIFade.Instance.FadeToBlack();

        // Wait until fade is fully black
        while (UIFade.Instance != null && UIFade.Instance.IsFading)
        {
            yield return null;
        }

        yield return new WaitForSeconds(0.5f); // optional pause after fade

        SceneManager.LoadScene(sceneToLoad);
    }
}
