using UnityEngine;
using UnityEngine.SceneManagement;
using Cinemachine;

public class CameraController : Singleton<CameraController>
{
    private CinemachineVirtualCamera cinemachineVirtualCamera;

    protected override void Awake()
    {
        base.Awake();
        SceneManager.sceneLoaded += OnSceneLoaded;
    }

    private void OnDestroy()
    {
        SceneManager.sceneLoaded -= OnSceneLoaded;
    }

    private void OnSceneLoaded(Scene scene, LoadSceneMode mode)
    {
        Debug.Log("Scene loaded: " + scene.name);
        SetPlayerCameraFollow();
    }

    public void SetPlayerCameraFollow()
    {
        cinemachineVirtualCamera = FindObjectOfType<CinemachineVirtualCamera>();

        if (cinemachineVirtualCamera == null)
        {
            Debug.LogError("❌ CinemachineVirtualCamera not found in scene.");
            return;
        }

        if (PlayerController.Instance == null)
        {
            Debug.LogError("❌ PlayerController.Instance is null.");
            return;
        }

        cinemachineVirtualCamera.Follow = PlayerController.Instance.transform;
        Debug.Log("✅ Camera is now following the player.");
    }
}